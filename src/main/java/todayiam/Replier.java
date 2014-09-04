package todayiam;

import org.springframework.social.twitter.api.Tweet;

import java.util.List;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public interface Replier {
    void reply(Tweet original, List<Tweet> tweets);
}
