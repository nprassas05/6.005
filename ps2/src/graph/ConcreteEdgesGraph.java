/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
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
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a weighted directed graph with vertices 
    //   and weighted edges between vertices.
    // Representation invariant:
    //   All edges have weight > 0
    //   each vertex has a distinct label
    //   each vertex involved in an edge is contained in the vertices set
    // Safety from rep exposure:
    //   TODO
    
    // TODO constructor
    public ConcreteEdgesGraph() {
        checkRep();
    }
    
    // check that the rep invariant is true
    private void checkRep() {
        for (Edge e: edges) {
            assert e.getWeight() > 0;
            assert(vertices.contains(e.getFrom()));
            assert(vertices.contains(e.getTo()));
        }
    }
    
    /*
     * (non-Javadoc)
     * @see graph.Graph#add(java.lang.Object)
     */
    @Override public boolean add(String vertex) {
        if (vertices.contains(vertex)) {
            return false;
        }
        vertices.add(vertex);
        return true;
    }
    
    /*
     * (non-Javadoc)
     * @see graph.Graph#set(java.lang.Object, java.lang.Object, int)
     */
    @Override public int set(String source, String target, int weight) {
        assert weight >= 0;
        vertices.add(source);
        vertices.add(target);
        Edge existingEdge = null;
        
        // check if an edge from source to target already exists
        for (Edge e: edges) {
            String eFrom = e.getFrom();
            String eTo = e.getTo();
            if (eFrom.equals(source) && eTo.equals(target)) {
                existingEdge = e;
                break;
            }
        }
        
        if (existingEdge != null) {
            int prevWeight = existingEdge.getWeight();
            edges.remove(existingEdge);
            
            if (weight > 0) {
                edges.add(existingEdge.setWeight(weight));
            }
            
            return prevWeight;
        }
        
        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }
        
        return 0;
    }
    
    /*
     * (non-Javadoc)
     * @see graph.Graph#remove(java.lang.Object)
     */
    @Override public boolean remove(String vertex) {
        if (!vertices.contains(vertex)) return false;
        
        vertices.remove(vertex);
        Iterator<Edge> iter = edges.iterator();
        
        while (iter.hasNext()) {
            Edge e = iter.next();
            String eFrom = e.getFrom();
            String eTo = e.getTo();
            
            if (eFrom.equals(vertex) || eTo.equals(vertex)) {
                iter.remove();
            }
        }
        
        checkRep();
        return true;
    }
    
    @Override public Set<String> vertices() {
        // make defensive copy of vertices to prevent rep exposure
        checkRep();
        return new HashSet<>(vertices);
    }
    
    /*
     * (non-Javadoc)
     * @see graph.Graph#sources(java.lang.Object)
     */
    @Override public Map<String, Integer> sources(String target) {
        checkRep();
        Map<String, Integer> sources = new HashMap<>();
        for (Edge e: edges) {
            if (e.getTo().equals(target)) {
                sources.put(e.getFrom(), e.getWeight());
            }
        }
        
        return sources;
    }
    
    /*
     * (non-Javadoc)
     * @see graph.Graph#targets(java.lang.Object)
     */
    @Override public Map<String, Integer> targets(String source) {
        checkRep();
        Map<String, Integer> targets = new HashMap<>();
        for (Edge e: edges) {
            if (e.getFrom().equals(source)) {
                targets.put(e.getTo(), e.getWeight());
            }
        }
        
        return targets;
    }
    
    // TODO toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Edge> sortedEdges = new ArrayList<>(edges);
        Collections.sort(sortedEdges, 
                        (a, b) -> a.getFrom().compareTo(b.getFrom())
        );
        
        int size = sortedEdges.size();
        for (int i = 0; i < size; i++) {
            String from = edges.get(i).toString().split("->")[0];
            String fromNext = i >= size - 1 ? from : edges.get(i + 1).toString().split("->")[0];
            sb.append(edges.get(i).toString());
            if (i < size - 1 && !from.equals(fromNext)) {
                sb.append('\n');
            }
        }
        
        return sb.toString();
    }
}

/**
 * Immutable Edge class.
 * This class is internal to the rep of ConcreteEdgesGraph.
 * 
 * <p>PS2 instructions: the specification and implementation of this class is
 * up to you.
 */
class Edge {
    
    // TODO fields
    final private String from;
    final private String to;
    final private int weight;
    
    // Abstraction function:
    //   AF(from, to, weight) = directed weighted edge with its source vertex,
    //   target vertex, and weight being equal to those in the rep.
    // Representation invariant:
    //   weight > 0
    //   
    // Safety from rep exposure:
    //   all fields are immutable and have private access
    
    // TODO constructor
    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        checkRep();
    }
    
    /*
     * check that the rep invariant holds
     */
    private void checkRep() {
        assert weight > 0;
    }
    
    /* change the weight of the edge
     * @param weight new weight to set the edge weight to, must be > 0
     */
    public Edge setWeight(int weight) {
        // return new Edge object to satisfy immutability constraint
        return new Edge(this.from, this.to, weight);
    }
    
    /*
     * @return the weight of the edge
     */
    public int getWeight() {
        checkRep();
        return weight;
    }
    
    /*
     * @return the source vertex of the edge
     */
    public String getFrom() {
        checkRep();
        return from;
    }
    
    /*
     * @return the target vertex of the edge
     */
    public String getTo() {
        checkRep();
        return to;
    }
    
    @Override
    public String toString() {
        checkRep();
        return new String("[(" + from + "->" + to + "): " + weight + "]");
    }
}