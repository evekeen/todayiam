package todayiam.utils;

import org.springframework.social.twitter.api.Tweet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public final class TweetUtils {
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
}
