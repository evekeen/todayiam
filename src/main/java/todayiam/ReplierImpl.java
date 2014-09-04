package todayiam;

import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.util.List;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public class ReplierImpl implements Replier {
    private Twitter twitter;

    public ReplierImpl(Twitter twitter) {
        this.twitter = twitter;
    }

    @Override
    public void reply(Tweet original, List<Tweet> tweets) {
        String text = tweets.size() + " users are feeling the same today. http://vk.com/public76644009";
        String message = getMentionString(original) + " " + text;
        try {
            twitter.timelineOperations().updateStatus(message);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw (e);
        }
    }

    private String getMentionString(Tweet original) {
        return "@" + original.getFromUser();
    }
}
