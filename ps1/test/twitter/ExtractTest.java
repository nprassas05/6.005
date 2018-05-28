/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    /*
     * Testing Strategy:
     * 
     * Break input list of tweets for getTimeSpan into the following cases:
     * list of two tweets with two different times
     * non-empty list of  six tweets with different times shuffled randomly
     * empty list of tweets in which the time interval should be of length 0
     *
     * 
     * Break input list of tweets for getMentioned users into following partitions:
     * empty list of tweets
     * tweets without any usernames
     * valid twitter usernames within text ex; @henry__899-87
     * invalid twitter usernames within text that have the @ character preceeding the name
     *                 ex; bitdiddle@mit.edu
     * twitter usernames that are valid up to a certain point, 
     *         ex; @john45()()) --> john45 would be the extracted username
     * same username mentioned with different cases for some or all characters
     * valid username at start of tweet
     * valid username at end of tweet
     * valid username in middle of tweet
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
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(2, "jane", "where is the juice #thirsty", d3);
    private static final Tweet tweet4 = new Tweet(2, "hansel", "here we go again #mad", d4);
    private static final Tweet tweet5 = new Tweet(2, "roger", "lets go cavaliers!", d5);
    private static final Tweet tweet6 = new Tweet(2, "benny", "wishing everyone a wonderful christmas", d6);
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    @Test
    public void testGetTimeSpanSixTweets() {
        List<Tweet> tweetList = Arrays.asList(tweet1, tweet2, tweet3, 
                                              tweet4, tweet5, tweet6);
        
        Collections.shuffle(tweetList);
        Timespan timespan = Extract.getTimespan(tweetList);
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d6, timespan.getEnd());
    }
    
    /* For this test the start and end time stamps should be equal, since
     * zero tweets can fit into a time interval of no time at all.
     */
    @Test
    public void testGetTimeSpanZeroTweets() {
        List<Tweet> list = new ArrayList<>();
        Timespan timespan = Extract.getTimespan(list);
        
        assertNotNull("should not be null", timespan);
        Instant start = timespan.getStart();
        Instant end = timespan.getEnd();
        assertEquals("expected start and end timestamps to be the same", end, start);
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersEmptyList() {
        List<Tweet> emptyList = new ArrayList<>();
        Set<String> mentionedUsers = Extract.getMentionedUsers(emptyList);
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedStartOfText() {
        Tweet tweetA = new Tweet(1, "alyssa", "@mikey why did you unfollow me?", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetA));
        assertEquals("expected size of set", 1, mentionedUsers.size());
        assertTrue(containsIgnoreCase(mentionedUsers,"mikey"));
    }
    
    @Test
    public void testGetMentionedEndOfText() {
        Tweet tweetA = new Tweet(1, "alyssa", "happy new year bro @mikey", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetA));
        assertEquals("expected size of set", 1, mentionedUsers.size());
        assertTrue(containsIgnoreCase(mentionedUsers,"mikey"));
    }
    
    @Test
    public void testGetMentionedMiddleOfText() {
        Tweet tweetA = new Tweet(1, "alyssa", "happy birthdy to @mikey my good buddy.", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetA));
        assertEquals("expected size of set", 1, mentionedUsers.size());
        assertTrue(containsIgnoreCase(mentionedUsers,"mikey"));
    }
    
    @Test
    public void testGetMentionedUsersInvalidUsernames() {
        Tweet tweetA = new Tweet(1, "alyssa", "happy birthdy to hero@mikey my good buddy.", d1);
        Tweet tweetB = new Tweet(1, "alyssa", "bitdiddle@mit.edu is a good student", d1);
        Tweet tweetC = new Tweet(1, "alyssa", "happy birthday thisguy@johnnboy", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetA, tweetB, tweetC));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersNoDuplicates() {
        Tweet tweetA = new Tweet(1, "alyssa", "happy birthdy to @mikey-123_8 my good buddy.", d1);
        Tweet tweetB = new Tweet(1, "alyssa", "hey @MikeY-123_8 what is up", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetA, tweetB));
        assertEquals("expected size of set", 1, mentionedUsers.size());
        assertTrue(containsIgnoreCase(mentionedUsers,"mikey-123_8"));
    }
    
    @Test
    public void testGetMentionedUsersValidWithTrailingInvalidChars() {
        Tweet tweetA = new Tweet(1, "alyssa", "waiting for @tim-234()() to arrive", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetA));
        assertEquals("expected size of set", 1, mentionedUsers.size());
        assertTrue(containsIgnoreCase(mentionedUsers,"tim-234"));
    }
    
    @Test
    public void testGetMentionedUsersMultipleUsersInSingleTweet() {
        // james-7 should not be extracted because the @ before username is preceded by "hey"
        Tweet tweetA = new Tweet(1, "alyssa", "@tim_34 hey@james-7 @_mulan", d1);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweetA));
        assertEquals("expected size of set", 2, mentionedUsers.size());
        assertTrue(containsIgnoreCase(mentionedUsers,"tim_34"));
        assertTrue(containsIgnoreCase(mentionedUsers,"_mulan"));
    }
    
    /**
     * Check if a collection of strings contains a given string, ignoring character
     * case while comparing strings.
     * @param collection
     * @param s
     * @return
     */
    private static boolean containsIgnoreCase(Collection<String> collection, String s) {
        for (String str: collection) {
            if (str.equalsIgnoreCase(s)) {
                return true;
            }
        }
        
        return false;
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */

}