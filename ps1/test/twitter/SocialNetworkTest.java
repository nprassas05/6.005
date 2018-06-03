/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * -------------------------------------------------------------------------
     * Testing strategy for guessFollowersGraph:
     * 
     * Aside from partitioning, ensure all users in the graph are either
     * tweet authors or users mentioned in a tweet.
     * 
     * Test cases will include some form of the following partitions:
     * Users who mention no other users in their tweets
     * Users who mention one or more users in their tweets
     * Users with multiple tweets with different users mentioned in those tweets
     * --------------------------------------------------------------------------
     * 
     * 
     * Testing strategy for influencers:
     * Empty followsGraph --> empty return list
     * followsGraph --> sorted correctly in descending order of number of followers
     * 
     * TODO: your testing strategies for these methods should go here.
     * See the ic03-testing exercise for examples of what a testing strategy comment looks like.
     * Make sure you have partitions.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-05-17T11:00:00Z");
    private static final Instant d4 = Instant.parse("2016-05-17T11:01:02Z");
    private static final Instant d5 = Instant.parse("2016-05-17T11:01:04Z");
    private static final Instant d6 = Instant.parse("2016-12-17T11:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "oh hey @jane rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(2, "jane", "where is the juice #thirsty", d3);
    private static final Tweet tweet4 = new Tweet(2, "hansel", "@alyssa here we go again #mad", d4);
    private static final Tweet tweet5 = new Tweet(2, "roger", "lets go cavaliers! @jane wohoo!", d5);
    private static final Tweet tweet6 = new Tweet(2, "benny", "wishing everyone a wonderful christmas", d6);
    private static final Tweet tweet7 = new Tweet(2, "benny", "@jane @roger @hansel love you guys", d6);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraph() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(
                                                    Arrays.asList(tweet1, tweet6, tweet7));
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        Set<String> bennyFollows = followsGraph.get("benny");
        assertEquals("expected set size", 3, bennyFollows.size());
        assertTrue(bennyFollows.contains("jane"));
        assertTrue(bennyFollows.contains("roger"));
        assertTrue(bennyFollows.contains("hansel"));
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("benny", new HashSet<>(Arrays.asList("tom", "jim")));
        followsGraph.put("henry_98", new HashSet<>(Arrays.asList("tom", "anna")));
        followsGraph.put("sarah", new HashSet<>(Arrays.asList("tom", "jim")));
        
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(3, influencers.size());
        assertEquals("tom", influencers.get(0));
        assertEquals("jim", influencers.get(1));
        assertEquals("anna", influencers.get(2));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */
}