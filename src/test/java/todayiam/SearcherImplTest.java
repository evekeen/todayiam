package todayiam;

import org.junit.Before;
import org.junit.Test;
import org.springframework.social.twitter.api.SearchOperations;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import todayiam.utils.TweetBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public class SearcherImplTest {
    private Searcher searcher;
    private KeywordsAnalyzer keywordAnalyzer = mock(KeywordsAnalyzer.class);
    private Twitter twitter = mock(Twitter.class);
    private SearchOperations searchOperations;
    private Date defaultDate;

    @Before
    public void init() throws ParseException {
        defaultDate = new SimpleDateFormat("yyyy-MM-dd").parse("2014-09-04");
        searchOperations = mock(SearchOperations.class);
        when(twitter.searchOperations()).thenReturn(searchOperations);
        when(keywordAnalyzer.findKeyWords(any(Tweet.class))).thenReturn(Arrays.asList("keywords"));
        searcher = new SearcherImpl(twitter, keywordAnalyzer);
    }

    @Test
    public void related() {
        Tweet originalTweet = new TweetBuilder("#todayiam #test feeling so damn good").withId(20).createdAt(defaultDate).build();
        Tweet relatedTweet = new TweetBuilder("test tweet").withFromUserId(3).withToUserId(4).build();
        SearchResults searchResults = mock(SearchResults.class);
        List<Tweet> expectedRelatedTweets = Arrays.asList(originalTweet, relatedTweet);
        when(searchResults.getTweets()).thenReturn(expectedRelatedTweets);
        when(searchOperations.search("#todayiam keywords since:2014-09-03")).thenReturn(searchResults);
        List<Tweet> related = searcher.findRelated(originalTweet);
        assertEquals(Arrays.asList(relatedTweet), related);
    }
}
