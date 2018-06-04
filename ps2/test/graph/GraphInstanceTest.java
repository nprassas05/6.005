/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    // 
    // Partition for add():
    // empty, non-empty
    // vertices that exist in graph beforehand, don't exist in graph beforehand
    // 
    //
    // Partition for set():
    // vertices that exist in graph beforehand, don't exist in graph beforehand
    // weight = 0, weight > 0
    //
    // Partition for remove():
    // vertices existent in the graph, non-existent in the graph
    // vertices with other edges, without other edges
    // 
    // Partition for vertices():
    // number of vertices is 0, > 0
    // 
    // Partition for sources():
    // number of edges pointing toward target is 0, > 0
    //
    // Partition for targets():
    // number of edges pointing out from source is 0, > 0
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // TODO other tests for instance methods of Graph
    @Test
    public void testAddToEmptyGraph() {
        Graph<String> graph = emptyInstance();
        assertTrue(graph.add("hello"));
    }
    
    @Test
    public void testAddToNonEmptyGraph() {
        Graph<String> graph = emptyInstance();
        assertTrue(graph.add("hello"));
        assertTrue(graph.add("goodbye"));
    }
    
    @Test
    public void testAddAlreadyExistingVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("hello");
        assertFalse(graph.add("hello"));
    }
    
    @Test
    public void testRemoveEmptyGraph() {
        Graph<String> g = emptyInstance();
        assertFalse(g.remove("jim"));
    }
    
    @Test
    public void testVerticesEmptyGraph() {
        assertEquals(0, emptyInstance().vertices().size());
    }
    
    @Test
    public void testVerticesNonEmptyGraph() {
        Graph<String> g = emptyInstance();
        g.add("roger");
        g.add("peanut");
        
        Set<String> vertices = g.vertices();
        
        assertEquals(2, vertices.size());
        assertTrue(vertices.contains("roger"));
        assertTrue(vertices.contains("peanut"));
    }
    
    @Test
    public void testSetNewEdge() {
        Graph<String> g = emptyInstance();
        g.add("roger");
        g.add("peanut");
        
        assertEquals(0, g.set("roger", "peanut", 23));
    }
    
    @Test
    public void testSetChangeEdgeWeight() {
        Graph<String> g = emptyInstance();
        g.add("roger");
        g.add("peanut");
        
        g.set("roger", "peanut", 23);
        assertEquals(23, g.set("roger", "peanut", 5));
    }
    
    @Test
    public void testSetNonExistingVertex() {
        Graph<String> g = emptyInstance();
        g.add("roger");
        // "sam" vertex doesn't exist but should get created in set below
        assertEquals(0, g.set("roger", "sam", 56));
    }
    
    @Test
    public void testSetDeleteEdge() {
        Graph<String> g = emptyInstance();
        g.set("roger", "sam", 40);
        assertEquals(40, g.set("roger", "sam", 0));
        assertEquals(0, g.set("roger", "sam", 3));
    }
    
    @Test
    public void testSourcesEmptyGraph() {
        assertEquals(0, emptyInstance().sources("tom").size());
    }
    
    @Test
    public void testSourcesNoSources() {
        Graph<String> g = emptyInstance();
        g.set("roger", "sam", 40);
        Map<String, Integer> sourcesToRoger = g.sources("roger");
        assertEquals(0, sourcesToRoger.size());
    }
    
    @Test
    public void testSourcesSomeSources() {
        Graph<String> g = emptyInstance();
        g.set("roger", "sam", 40);
        g.set("jim", "sam", 90);
        Map<String, Integer> sourcesMap = g.sources("sam");
        
        assertEquals(2, sourcesMap.size());
        assertEquals(40, (int) sourcesMap.get("roger"));
        assertEquals(90, (int) sourcesMap.get("jim"));
    }
    
    @Test
    public void testTargetsEmptyGraph() {
        assertEquals(0, emptyInstance().targets("tom").size());
    }
    
    @Test
    public void testTargetsNoTargets() {
        Graph<String> g = emptyInstance();
        g.set("roger", "sam", 40);
        assertEquals(0, g.targets("sam").size());
    }
    
    @Test
    public void testTargetsSomeTargets() {
        Graph<String> g = emptyInstance();
        g.set("roger", "sam", 40);
        g.set("roger", "april", 30);
        Map<String, Integer> targetsMap = g.targets("roger");
        assertEquals(2, targetsMap.size());
        assertEquals(40, (int) targetsMap.get("sam"));
        assertEquals(30, (int) targetsMap.get("april"));
    }
}