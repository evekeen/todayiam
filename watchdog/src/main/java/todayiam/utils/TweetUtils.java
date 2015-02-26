package todayiam.utils;

import com.google.common.collect.Lists;
import org.springframework.social.twitter.api.Tweet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public final class TweetUtils {
    private static final SimpleDateFormat SUBJECT_FORMAT = new SimpleDateFormat("MM.dd.yyyy");

    private TweetUtils() {}

    public static String getSinceDate(Tweet tweet) {
        Date createdAt = tweet.getCreatedAt();
        Calendar cal = Calendar.getInstance();
        cal.setTime(createdAt);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date oneDayBefore = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(oneDayBefore);
    }

    public static List<String> getWords(Tweet tweet) {
        return Lists.newArrayList(tweet.getText().split("\\s+"));
    }

    public static String prettyTweetSubject(Tweet t) {
        return SUBJECT_FORMAT.format(t.getCreatedAt()) + ": " + t.getText();
    }

}
