/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph<String>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   TODO
    
    // TODO tests for ConcreteEdgesGraph.toString()
    @Test
    public void testToString() {
        Graph<String> g = new ConcreteEdgesGraph<String>();
        g.set("jim", "henry", 45);
        g.set("jim", "abe", 2);
        g.set("yen", "jen", 3);
        
        System.out.println(g);
    }
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    // newly constructed edges, edges that change their edge weight
    @Test
    public void testEdge() {
        Edge<String> e = new Edge<String>("here", "there", 4);
        System.out.println(e.toString());
    }
    
    // TODO tests for operations of Edge
    @Test
    public void testEdgeGetWeightSetWeight() {
        Edge<String> e = new Edge<String>("here", "there", 4);
        assertEquals(4, e.getWeight());
        Edge<String> r = e.setWeight(34);
        assertEquals(34, r.getWeight());
        assertTrue(r != e);
    }
}