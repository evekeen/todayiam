package todayiam;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.springframework.social.twitter.api.Tweet;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordsAnalyzerImpl implements KeywordsAnalyzer {
    public static final int DEFAULT_KEY_WORD_NUMBER = 2;
    private int keyWordNumber = DEFAULT_KEY_WORD_NUMBER;
    private int minLength = 3;

    public void setKeyWordNumber(int keyWordNumber) {
        this.keyWordNumber = keyWordNumber;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    @Override public List<String> findKeyWords(Tweet tweet) {
        List<String> words = Lists.newArrayList(tweet.getText().split("\\s+"));
        words.sort((s1, s2) -> s2.length() - s1.length());
        words.removeIf(s -> s.toLowerCase().contains("#todayiam"));
        words.removeIf(s -> s.toLowerCase().contains("#test"));
        FluentIterable<String> fluent = FluentIterable.from(words).transform(w -> {
            Pattern p = Pattern.compile("^(\\w+)");
            Matcher matcher = p.matcher(w);
            return matcher.find() ? matcher.group(1) : "";
        });
        fluent = fluent.filter(s -> s.length() >= minLength);
        words = fluent.toImmutableList();
        int high = Math.min(words.size(), keyWordNumber);
        return words.subList(0, high);
    }
}