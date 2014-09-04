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
    private KeywordsAnalyzerImpl keywordsAnalyzer;

    @Before
    public void init() {
        keywordsAnalyzer = new KeywordsAnalyzerImpl();
        keywordsAnalyzer.setKeyWordNumber(3);
        keywordsAnalyzer.setMinLength(3);
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

    @Test
    public void symbols() {
        Tweet tweet = new TweetBuilder("#todayiam #test feeling good!").build();
        List<String> keyWords = keywordsAnalyzer.findKeyWords(tweet);
        assertEquals(2, keyWords.size());
        assertEquals("feeling", keyWords.get(0));
        assertEquals("good", keyWords.get(1));
    }

    @Test
    public void shortWord() {
        Tweet tweet = new TweetBuilder("#todayiam #test wide awake at 09:30").build();
        List<String> keyWords = keywordsAnalyzer.findKeyWords(tweet);
        assertEquals(2, keyWords.size());
        assertEquals("awake", keyWords.get(0));
        assertEquals("wide", keyWords.get(1));
    }
}
