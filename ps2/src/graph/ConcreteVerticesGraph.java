/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   AF(vertices) = directed weighted graph containing every vertex
    //   in vertices.
    // Representation invariant:
    //   Every String label that represents the target vertex of an edge
    //   is the string label of some vertex in the graph.  In other words,
    //   for every String label L in v.targets().keySet() for any and every vertex v,
    //   there exists a vertex VX in vertices such that VX.getLabel().equals(L).
    // Safety from rep exposure:
    //   vertices is a mutable list, but it is never returned or passed as an argument
    //   in any public methods.
    
    // TODO constructor
    
    // TODO checkRep
    public void checkRep() {
        assert vertices != null;
        Set<String> vertexLabelSet = new HashSet<>();
        
        for (Vertex v: vertices) {
            vertexLabelSet.add(v.getLabel());
        }
        
        for (Vertex v: vertices) {
            Map<String, Integer> targetsFromV = v.targets();
            for (String label: targetsFromV.keySet()) {
                assert(vertexLabelSet.contains(label));
            }
        }
    }
    
    @Override public boolean add(String vertex) {
        for (Vertex v: vertices) {
            String label = v.getLabel();
            if (label.equals(vertex)) {
                return false;
            }
        }
        
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }
    
    @Override public int set(String source, String target, int weight) {
        int prevWeight = 0;
        add(source);
        add(target);
        
        Vertex sourceVertex = getVertex(source);
        if (sourceVertex.containsEdgeTo(target)) {
            prevWeight = sourceVertex.edgeWeight(target);
        }
        
        sourceVertex.addEdge(target, weight);
        return prevWeight;
    }
    
    @Override public boolean remove(String vertex) {
        boolean existed = false;
        
        for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext(); ) {
            Vertex v = iter.next();
            String label = v.getLabel();
            if (label.equals(vertex)) {
                iter.remove();
                existed = true;
            }
            
            v.removeEdge(vertex);
        }
        
        return existed;
    }
    
    @Override public Set<String> vertices() {
        Set<String> verticesSet = new HashSet<>();
        for (Vertex v: this.vertices) {
            verticesSet.add(v.getLabel());
        }
        
        return verticesSet;
    }
    
    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        for (Vertex v: vertices) {
            Map<String, Integer> targets = v.targets();
            if (targets.containsKey(target)) {
                sources.put(v.getLabel(), targets.get(target));
            }
        }
        return sources;
    }
    
    @Override public Map<String, Integer> targets(String source) {
        for (Vertex v: vertices) {
            String label = v.getLabel();
            if (label.equals(source)) {
                return v.targets();
            }
        }
        
        return new HashMap<>();
    }
    
    /**
     * @return a string representation of the graph, which will consist
     * of individual string representations of each vertex in the graph
     * separated by newlines.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Vertex> iter = vertices.iterator(); iter.hasNext(); ) {
            Vertex v = iter.next();
            sb.append(v.toString());
            if (iter.hasNext()) {
                sb.append('\n');
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Find and return the vertex with the given string label.
     * 
     * @param vString the label to compare against, requires that exactly one
     *        vertex in the graph has vString as its label.
     * @return the vertex whose label is vString.
     * @throws RuntimeException if no vertex in vertices has vString as its label.
     */
    private Vertex getVertex(String vString) {
        for (Vertex v: vertices) {
            if (v.getLabel().equals(vString)) {
                return v;
            }
        }
        
        throw new RuntimeException("Invalid source vertex label not found in graph");
    }
}

/**
 * TODO specification
 * Mutable.
 * This class is internal to the rep of ConcreteVerticesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Vertex {
    
    // fields
    private String label;
    private Map<String, Integer> targets = new HashMap<>();
    
    // Abstraction function:
    //   AF(label, targets) = vertex with given label containing edges
    //                        to vertices with labels in targets.keySet()
    // Representation invariant:
    //   weights of all edges from this vertex > 0, 
    //   targets.get(s) > 0 for all s in targets.keySet()
    // Safety from rep exposure:
    //   - id is an immutable string field
    //   - targets is a mutable map, but it is defensively copied when returned
    //   from a method and is never a method parameter.
    
    /**
     * construct a new vertex instance with the given
     * @param label the label of the vertex instance upon initialization.
     */
    public Vertex(String label) {
        this.label = label;
    }
    
    // TODO checkRep
    private void checkRep() {
        assert label != null;
        for (Integer w: targets.values()) {
            assert w > 0;
        }
    }
    
    /**
     * 
     * @param target
     * @param weight
     */
    public void addEdge(String target, int weight) {
        targets.put(target, weight);
    }
    
    /**
     * 
     * @param target
     */
    public void removeEdge(String target) {
        targets.remove(target);
    }
    
    /**
     * 
     * @param t
     * @return
     */
    public int edgeWeight(String t) {
        return targets.get(t);
    }
    
    /**
     * 
     * @return
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * check if an edge pointing to a given label exists.
     * @param s the string to check against.
     * @return true if this vertex has an edge marked to the given label,
     *         false otherwise.
     */
    public boolean containsEdgeTo(String s) {
        return targets.containsKey(s);
    }
    
    /**
     * @return a string representation of the vertex, which will be the
     * id of the vertex along with all neighbor vertices whom the vertex
     * instance has an edge pointing to.
     */
    @Override
    public String toString() {
        checkRep();
        return label + ": " + targets.toString();
    }
    
    public Map<String, Integer> targets() {
        return new HashMap<>(this.targets);
    }
}