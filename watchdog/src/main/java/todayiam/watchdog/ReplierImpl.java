package todayiam.watchdog;

import com.google.common.base.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.util.List;

import static com.google.common.collect.FluentIterable.from;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public class ReplierImpl implements Replier {
    public static final Function<Tweet, String> TO_QUERY_PARAM = new Function<Tweet, String>() {
        @Override public String apply(Tweet tweet) {
            return "id=" + tweet.getId();
        }
    };
    public static final String HOST = "ec2-54-186-255-239.us-west-2.compute.amazonaws.com";
    private Twitter twitter;

    public ReplierImpl(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public void reply(Tweet original, List<Tweet> tweets) {
        String text = tweets.size() + " users are feeling the same today. " + prepareUrl(original, tweets);
        String message = getMentionString(original) + " " + text;
        try {
            twitter.timelineOperations().updateStatus(message);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String prepareUrl(Tweet original, List<Tweet> tweets) {
        String ids = StringUtils.join(from(tweets).transform(TO_QUERY_PARAM), "&");
        return "http://" + HOST + "/matches/" + original.getId() + "?" + ids;
    }

    private String getMentionString(Tweet original) {
        return "@" + original.getFromUser();
    }
}
