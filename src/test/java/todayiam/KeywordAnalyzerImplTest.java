package todayiam;

import org.junit.Before;
import org.junit.Test;
import org.springframework.social.twitter.api.Tweet;
import todayiam.utils.TweetBuilder;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public class KeywordAnalyzerImplTest {
    private KeywordsAnalyzer keywordsAnalyzer;

    @Before
    public void init() {
        keywordsAnalyzer = new KeywordsAnalyzerImpl();
    }

    @Test
    public void longText() {
        Tweet tweet = new TweetBuilder("#todayiam #test I wanna to test every single peace of this wonderful project").build();
        List<String> keyWords = keywordsAnalyzer.findKeyWords(tweet);
        assertEquals(3, keyWords.size());
        assertEquals("wonderful", keyWords.get(0));
        assertEquals("project", keyWords.get(1));
        assertEquals("single", keyWords.get(2));
    }

    @Test
    public void shortText() {
        Tweet tweet = new TweetBuilder("#todayiam #test feeling sad").build();
        List<String> keyWords = keywordsAnalyzer.findKeyWords(tweet);
        assertEquals(2, keyWords.size());
        assertEquals("feeling", keyWords.get(0));
        assertEquals("sad", keyWords.get(1));
    }
}