/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    // Testing strategy
    // 
    //
    // partition poem(input) into:
    // number of bridge words inserted between two adjacent words in output: 0, 1, n
    // weight of edges in affinity graph: 0, 1, n
    // case of input word characters: lower case, upper case
    // 
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    /* Test where input text has zero bridge words based on file corpus.  In this
     * case the output poem text should be identical to the input text.
     */
    @Test
    public void testPoemZeroBridgeWords() throws IOException {
        GraphPoet gp = new GraphPoet(new File("test/poet/sample1.txt"));
        String input = "Here we go, into the night";
        String output = gp.poem(input);
        
        assertEquals(input, output);
    }
    
    /*
     * covers cases where one bridge word exists for some adcacent words
     * and the weights of all edges are 1 in the affinity graph.
     */
    @Test
    public void testPoemZeroOrOneBridgeWords() throws IOException {
        GraphPoet gp = new GraphPoet(new File("test/poet/sample1.txt"));
        String input = "Seek to explore new and exciting synergies!";
        
        String expectedOutput = "Seek to explore strange new life and exciting synergies!";
        assertEquals(expectedOutput, gp.poem(input));
    }
    
    /*
     * covers case where multiple bridge words exist, and the one corresponding
     * to a higher edge weight in the affinity graph should be selected.
     */
    @Test
    public void testPoemNBridgeWords() throws IOException {
        GraphPoet gp = new GraphPoet(new File("test/poet/sample2.txt"));
        String input = "Seek to explore new and exciting synergies!";
        String expectedOutput = "Seek to explore amazing new life and exciting synergies!";
        System.out.println(gp.poem(input));
        assertEquals(expectedOutput, gp.poem(input));
    }
}