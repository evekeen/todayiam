package todayiam.watchdog;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Throwables.propagate;
import static java.lang.Math.max;
import static org.apache.commons.lang3.StringUtils.join;
import static todayiam.utils.TweetUtils.getSinceDate;
import static todayiam.utils.TweetUtils.prettyTweetSubject;

/**
 * Author: Alexander Ivkin
 * Date: 02/09/14
 */
public class SearcherImpl implements Searcher {
    private Twitter twitter;
    private AtomicLong lastScanned = new AtomicLong();
    private String rootPath = System.getProperty("user.home") + File.separator + ".todayiam";
    private KeywordsAnalyzer keywordsAnalyzer;
    private static Logger logger = LoggerFactory.getLogger(SearcherImpl.class);

    public SearcherImpl(Twitter twitter, KeywordsAnalyzer keywordsAnalyzer) {
        this.twitter = twitter;
        this.keywordsAnalyzer = keywordsAnalyzer;
        loadLastId();
    }

    private void loadLastId() {
        File root = new File(rootPath);
        String fileName = idFilePath();
        if (!root.exists() || !root.isDirectory()) {
            createNewIdFile(root, fileName);
        } else {
            loadLastId(fileName);
        }
    }

    private void loadLastId(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            lastScanned.set(Long.parseLong(reader.readLine()));
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private void createNewIdFile(File root, String fileName) {
        if (!root.mkdir()) throw new RuntimeException("Cannot create working directory:" + root);
        File lastIdFile = new File(fileName);
        try {
            boolean status = lastIdFile.createNewFile();
            if (!status) throw new RuntimeException("Cannot create a last id file");
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    @Override public Tweet getById(long id) {
        return twitter.timelineOperations().getStatus(id);
    }

    @Override public List<Tweet> findNew() {
        final long sinceId = lastScanned.get();
        SearchParameters query = new SearchParameters("#todayiam").sinceId(sinceId);
        List<Tweet> tweets = twitter.searchOperations().search(query).getTweets();
        if (tweets.size() > 0) {
            Iterable<String> subjects = FluentIterable.from(tweets).transform(new Function<Tweet, String>() {
                @Override public String apply(Tweet t) {
                    updateLastScanId(t, sinceId);
                    return prettyTweetSubject(t);
                }
            });
            logger.debug(join(subjects, "\n"));
            saveLastId();
        }
        return tweets;
    }

    private void updateLastScanId(Tweet t, long sinceId) {
        long curSinceId = sinceId;
        while (!lastScanned.compareAndSet(curSinceId, max(curSinceId, t.getId()))) {
            curSinceId = lastScanned.get();
        }
    }

    private void saveLastId() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(idFilePath())))) {
            writer.write(lastScanned + "");
        } catch (IOException e) {
            throw propagate(e);
        }
    }

    private String idFilePath() {
        return rootPath + File.separator + "lastid.txt";
    }

    @Override public List<Tweet> findRelated(Tweet tweet) {
        return findRelated(tweet, keywordsAnalyzer.findKeyWords(tweet));
    }

    @Override public List<Tweet> findRelated(Long id) {
        return findRelated(getById(id));
    }

    private List<Tweet> findRelated(final Tweet tweet, List<String> keyWords) {
        String keyWordsQuery = join(keyWords, " ");
        String since = " since:" + getSinceDate(tweet);
        String query = "#todayiam " + keyWordsQuery + since;
        logger.debug("query: " + query);
        List<Tweet> related = twitter.searchOperations().search(query).getTweets();
        return FluentIterable.from(related).filter(new Predicate<Tweet>() {
            @Override public boolean apply(Tweet t) {
                return t.getFromUserId() != tweet.getFromUserId();
            }
        }).toList();
    }
}
