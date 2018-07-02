/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<String>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   TODO
    
    // TODO tests for ConcreteVerticesGraph.toString()
    @Test
    public void testToString() {
        Graph<String> g = new ConcreteVerticesGraph<String>();
        g.set("jim", "henry", 45);
        g.set("jim", "abe", 2);
        g.set("yen", "jen", 3);
        
        /* Not a great idea since the toString() method relies on the toString()
         * method of java's map interface, probably should refactor this toString()
         * at some point.  If map changes its toString() method, this test will fail.
         * */
        assertTrue(g.toString().indexOf("jim: {henry=45, abe=2}") >= 0);
        assertTrue(g.toString().indexOf("abe: {}") >= 0);
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //   addEdge:
    //     number of edges = 0, 1, n
    //   removeEdge:
    //     number of edges = 0, 1, n
    //   constructor:
    //     just one test with initial label.
    
    @Test
    public void testVertexConstructor() {
        Vertex<String> v = new Vertex<String>("abe");
        assertEquals("abe", v.getLabel());
        assertEquals(0, v.targets().size());
    }
    
    @Test
    public void testAddEdge() {
        Vertex<String> v = new Vertex<String>("abe");
        v.addEdge("tom", 5);
        assertEquals(5, v.edgeWeight("tom"));
    }
    
    @Test
    public void testRemoveEdge() {
        Vertex<String> v = new Vertex<String>("abe");
        v.addEdge("tom", 5);
        v.removeEdge("tom");
        assertFalse(v.containsEdgeTo("tom"));
    }
}