package todayiam.watchdog;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.springframework.social.twitter.api.Tweet;
import todayiam.utils.TweetUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.lowerCase;

public class KeywordsAnalyzerImpl implements KeywordsAnalyzer {
    public static final int DEFAULT_KEY_WORD_NUMBER = 2;
    public static final Function<String, String> LOWER_CASE = new Function<String, String>() {
        @Override public String apply(String s) {
            return lowerCase(s);
        }
    };
    public static final Comparator<String> LENGTH_COMPARATOR = new Comparator<String>() {
        @Override public int compare(String s1, String s2) {
            return s2.length() - s1.length();
        }
    };
    private int keyWordNumber = DEFAULT_KEY_WORD_NUMBER;
    private int minLength = 3;

    private static final List<String> KNOWN_WORDS = Arrays.asList("today", "feeling", "#todayiam", "#test", "i'm");
    public static final Predicate<String> KNOW_WORDS_FILTER = new Predicate<String>() {
        @Override public boolean apply(String s) {
            return !KNOWN_WORDS.contains(s);
        }
    };

    public static final Pattern PUNCTUATION_PATTERN = Pattern.compile("(^.*)[!?.,]+$");
    public static final Function<String, String> FILTER_OUT_PUNCTUATION = new Function<String, String>() {
        @Override public String apply(String w) {
            Matcher matcher = PUNCTUATION_PATTERN.matcher(w);
            return matcher.find() ? matcher.group(1) : w;
        }
    };
    public void setKeyWordNumber(int keyWordNumber) {
        this.keyWordNumber = keyWordNumber;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    @Override public List<String> findKeyWords(Tweet tweet) {
        List<String> words = TweetUtils.getWords(tweet);
        Collections.sort(words, LENGTH_COMPARATOR);
        FluentIterable<String> lowerCaseWords = FluentIterable.from(words).transform(LOWER_CASE);
        FluentIterable<String> noPunctuation = lowerCaseWords.transform(FILTER_OUT_PUNCTUATION);
        FluentIterable<String> knownWordsFiltered = getIfNonEmpty(noPunctuation, noPunctuation.filter(KNOW_WORDS_FILTER));
        FluentIterable<String> lengthFiltered = getIfNonEmpty(knownWordsFiltered, knownWordsFiltered.filter(minLengthFilter()));
        return getSubList(lengthFiltered);
    }

    private List<String> getSubList(FluentIterable<String> fluent) {
        List<String> words = fluent.toList();
        int high = Math.min(words.size(), keyWordNumber);
        return words.subList(0, high);
    }

    private Predicate<String> minLengthFilter() {
        return new Predicate<String>() {
            @Override public boolean apply(String s) {
                return s.length() >= minLength;
            }
        };
    }

    private static FluentIterable<String> getIfNonEmpty(FluentIterable<String> collection, FluentIterable<String> tryCollection) {
        return tryCollection.size() > 0 ? tryCollection : collection;
    }
}