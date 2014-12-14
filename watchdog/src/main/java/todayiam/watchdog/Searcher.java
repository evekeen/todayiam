package todayiam.watchdog;

import org.springframework.social.twitter.api.Tweet;

import java.util.List;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public interface Searcher {
    List<Tweet> findNew();

    List<Tweet> findRelated(Tweet tweet);

    Tweet getById(long id);
}
