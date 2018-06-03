/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.Instant;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        /* If the list is empty, return a Timespan with arbitrary 
         * start and end time stamps that are identical.
         */
        if (tweets.isEmpty()) {
            String timeStampString = "2016-02-17T10:00:00Z";
            return new Timespan(Instant.parse(timeStampString), 
                                Instant.parse(timeStampString));
        }
        
        Instant start = getMinTimeStamp(tweets);
        Instant end = getMaxTimeStamp(tweets);
        return new Timespan(start, end);
    }
    
    /**
     * 
     * @param tweets requires tweets.size() > 0
     *        tweets list is not modified by this method.
     * @return the earliest chronological timestamp contained within any of the tweets
     */
    private static Instant getMinTimeStamp(final List<Tweet> tweets) {
        assert tweets.size() > 0;
        Instant minTimeStamp = Instant.MAX;
        
        for (Tweet tweet: tweets) {
            Instant tweetTimeStamp = tweet.getTimestamp();
            if (minTimeStamp.isAfter(tweetTimeStamp)) {
                minTimeStamp = tweetTimeStamp;
            }
        }
        
        return minTimeStamp;
    }
    
    /**
     * 
     * @param tweets requires tweets.size() > 0
     *        tweets list is not modified by this method.
     * @return the latest chronological timestamp contained with any of the tweets
     */
    private static Instant getMaxTimeStamp(final List<Tweet> tweets) {
        assert tweets.size() > 0;
        Instant maxTimeStamp = Instant.MIN;
        
        for (Tweet tweet: tweets) {
            Instant tweetTimeStamp = tweet.getTimestamp();
            if (maxTimeStamp.isBefore(tweetTimeStamp)) {
                maxTimeStamp = tweetTimeStamp;
            }
        }
        
        return maxTimeStamp;
    }

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentionedUsers = new HashSet<>();
        
        for (Tweet t: tweets) {
            Set<String> subsetMentionedUsers = getMentionedUsersInText(t.getText());
            mentionedUsers.addAll(subsetMentionedUsers);
        }
        
        return mentionedUsers;
    }
    
    /**
     * Get the set of valid usernames contained within one string of text.
     * @param text a string of text, ideally from a tweet but not required.
     * @return the set of valid usernames contained within the string of text.
     *         promise: All usernames in the set will have all alphabetic letters in lower-case form.
     */
    protected static Set<String> getMentionedUsersInText(String text) {
        Set<String> mentionedUsers = new HashSet<>();
        
        int i = 0;
        int textLen = text.length();
        
        while (i < textLen) {
            char c = text.charAt(i);
            
            /* set prevChar to an arbitrary non-valid twitter username char if i = 0
             * or simply the previous character if i > 0.
             */
            char prevChar = i == 0 ? '@' : text.charAt(i - 1);
            
            if (c == '@' && !validTwitterUserChar(prevChar)) {
                ++i;
                StringBuilder sb = new StringBuilder();
                while (i < textLen && validTwitterUserChar(text.charAt(i))) {
                    sb.append(text.charAt(i));
                    ++i;
                }
                
                /* Add the username with all letters converted to lower-case */
                if (sb.length() > 0) {
                    mentionedUsers.add(sb.toString().toLowerCase());
                }
            }
            
            ++i;
        }
        
        return mentionedUsers;
    }
    
    /**
     * Determine whether or not a given character is a character that
     * can be part of a valid twitter username.  A valid character
     * in a twitter username is either a letter, digit, hyphen, or underscore.
     * @param c
     * @return
     */
    private static boolean validTwitterUserChar(char c) {
        if (c == '-' || c == '_' ||
            Character.isDigit(c) || Character.isLetter(c)) return true;
        
        return false;
    }
}