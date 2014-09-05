package todayiam;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.twitter.api.Tweet;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeywordsAnalyzerImpl implements KeywordsAnalyzer {
    public static final int DEFAULT_KEY_WORD_NUMBER = 2;
    private int keyWordNumber = DEFAULT_KEY_WORD_NUMBER;
    private int minLength = 3;
    private static final List<String> knownWords = Arrays.asList("today", "feeling", "#todayiam", "#test", "i'm");

    public void setKeyWordNumber(int keyWordNumber) {
        this.keyWordNumber = keyWordNumber;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    @Override public List<String> findKeyWords(Tweet tweet) {
        List<String> words = Lists.newArrayList(tweet.getText().split("\\s+"));
        words.sort((s1, s2) -> s2.length() - s1.length());
        words.forEach(StringUtils::lowerCase);
        FluentIterable<String> fluent = FluentIterable.from(words).transform(w -> {
            Pattern p = Pattern.compile("(^.*)[!?.,]+$");
            Matcher matcher = p.matcher(w);
            return matcher.find() ? matcher.group(1) : w;
        });
        FluentIterable<String> knownWordsFiltered = fluent.filter(s -> !knownWords.contains(s));
        fluent = getIfNonEmpty(fluent, knownWordsFiltered);
        FluentIterable<String> lengthFiltered = fluent.filter(s -> s.length() >= minLength);
        fluent = getIfNonEmpty(fluent, lengthFiltered);
        return getSubList(fluent);
    }

    private List<String> getSubList(FluentIterable<String> fluent) {
        List<String> words;
        words = fluent.toList();
        int high = Math.min(words.size(), keyWordNumber);
        return words.subList(0, high);
    }

    private static FluentIterable<String> getIfNonEmpty(FluentIterable<String> collection, FluentIterable<String> tryCollection) {
        return tryCollection.size() > 0 ? tryCollection : collection;
    }
}