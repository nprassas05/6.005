/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    // Abstraction function:
    //   AF(graph) = affinity graph with edge weights for any edge s -> t
    //   being the number of times t follows s in a sentence based on a given
    //   corpus.
    // Representation invariant:
    //   All words in affinity graph are lower case.
    // Safety from rep exposure:
    //   Constructor only takes a file argument, so rep is not exposed.
    //   All public methods return immutable strings.
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(corpus));
        String line;
        String prevWord = "";
        Map<String, Integer> targetsFromPrevWord;
        
        while ((line = reader.readLine()) != null) {
            Scanner scanner = new Scanner(line);
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                
                if (!prevWord.isEmpty()) {
                    targetsFromPrevWord = graph.targets(prevWord);
                    int weight = targetsFromPrevWord.getOrDefault(word, 0);
                    graph.set(prevWord, word, weight + 1);
                }
                
                prevWord = word;
            }
            
            scanner.close();
        }
        
        reader.close();
        checkRep();
    }
    
    // checkRep
    public void checkRep() {
        for (String s: graph.vertices()) {
            assert s.equals(s.toLowerCase());
        }
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     *        requires string is non-empty
     * @return poem (as described above)
     */
    public String poem(String input) {
        checkRep();
        
        if (input.isEmpty()) {
            throw new IllegalArgumentException("empty string "
                    + "cannot be used to generate a poem");
        }
        
        String[] words = input.split("\\s+");
        List<String> poemWords = new ArrayList<>();
        poemWords.add(words[0]);
        
        for (int i = 1; i < words.length; i++) {
            String prevWord = words[i - 1];
            String word = words[i];
            String bridgeWord = maxBridgeWord(prevWord, word);
            
            if (!bridgeWord.isEmpty()) {
                poemWords.add(bridgeWord);
            }
            
            poemWords.add(word);
        }
        
        StringBuilder poemSB = new StringBuilder();
        for (Iterator<String> iter = poemWords.iterator(); iter.hasNext(); ) {
            poemSB.append(iter.next());
            if (iter.hasNext()) poemSB.append(" ");
        }
        
        return poemSB.toString();
    }
    
    /**
     * Find the bridge word such that the two edge path
     * from the sourceWord to the bridge word to the target
     * word has the largest weight among all two edge paths
     * from the source to target word.
     * 
     * @param sourceWord
     * @param targetWord
     * @return the bridge word that is part of the largest weighted path
     *         sourceWord -> bridgeWord -> targetWord among all possibilities
     *         according to our affinity graph.
     *         Return an empty string if no such bridge word exists.
     */
    private String maxBridgeWord(String sourceWord, String targetWord) {
        String maxBridgeWord = "";
        int maxPathWeight = 0;
        
        
        /*
         *  convert source and target words to lower case before getting edge weight maps
         *  since all our rep requires are vertex string labels to be lower case.
         */
        Map<String, Integer> targetsFromSource = graph.targets(sourceWord.toLowerCase());
        Map<String, Integer> sourcesFromTarget = graph.sources(targetWord.toLowerCase());
        
        for (String t: targetsFromSource.keySet()) {
            // check if t is a bridge word.
            if (sourcesFromTarget.containsKey(t)) {
                int pathWeight = targetsFromSource.get(t) + sourcesFromTarget.get(t);
                if (maxPathWeight < pathWeight) {
                    maxPathWeight = pathWeight;
                    maxBridgeWord = t;
                }
            }
        }
        
        return maxBridgeWord;
    }
    
    @Override
    public String toString() {
        return graph.toString();
    }
}