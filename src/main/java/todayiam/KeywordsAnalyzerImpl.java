package todayiam;

import com.google.common.collect.Lists;
import org.springframework.social.twitter.api.Tweet;

import java.util.List;

public class KeywordsAnalyzerImpl implements KeywordsAnalyzer {
    @Override public List<String> findKeyWords(Tweet tweet) {
        List<String> words = Lists.newArrayList(tweet.getText().split("\\s+"));
        words.sort((s1, s2) -> s2.length() - s1.length());
        words.removeIf(s -> s.toLowerCase().contains("#todayiam"));
        words.removeIf(s -> s.toLowerCase().contains("#test"));
        int high = Math.min(words.size(), SearcherImpl.KEY_WORD_NUMBER);
        return words.subList(0, high);
    }
}