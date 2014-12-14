package todayiam.watchdog;

import com.google.common.util.concurrent.AbstractScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public class Scheduler extends AbstractScheduledService {
    private Searcher searcher;
    private Replier replier;

    private static Logger logger = LoggerFactory.getLogger(Scheduler.class);

    public Scheduler(Searcher searcher, Replier replier) {
        this.searcher = searcher;
        this.replier = replier;
    }

    @Override
    protected void runOneIteration() throws Exception {
        logger.info("Searching for new tweets....");
        List<Tweet> tweets = searcher.findNew();
        for (Tweet tweet : tweets) {
            List<Tweet> similar = searcher.findRelated(tweet);
            logger.info("Found " + similar.size() + " similar tweets");
            if (similar.size() > 0) replier.reply(tweet, similar);
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(0, 10, TimeUnit.SECONDS);
    }

    @Override
    protected void startUp() {
        logger.info("StartUp scheduler....");
    }


    @Override
    protected void shutDown() {
        logger.warn("Shutdown scheduler...");
    }
}