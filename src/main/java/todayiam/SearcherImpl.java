package todayiam;

import com.google.common.collect.FluentIterable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.join;
import static todayiam.utils.TweetUtils.getSinceDate;

/**
 * Author: Alexander Ivkin
 * Date: 02/09/14
 */
public class SearcherImpl implements Searcher {
    private Twitter twitter;
    private long lastScanned;
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
        String fileName = rootPath + File.separator + "lastid.txt";
        if (!root.exists() || !root.isDirectory()) {
            root.mkdir();
            File lastIdFile = new File(fileName);
            try {
                boolean status = lastIdFile.createNewFile();
                if (!status) throw new RuntimeException("Cannot create a last id file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                lastScanned = Long.parseLong(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override public List<Tweet> findNew() {
        SearchParameters query = new SearchParameters("#todayiam #test").sinceId(lastScanned);
        List<Tweet> tweets = twitter.searchOperations().search(query).getTweets();
        final SimpleDateFormat format = new SimpleDateFormat("MM.dd.yyyy");
        if (tweets.size() > 0) {
            FluentIterable<String> subjects = FluentIterable.from(tweets).transform(tweet -> {
                lastScanned = Math.max(lastScanned, tweet.getId());
                return format.format(tweet.getCreatedAt()) + ": " + tweet.getText();
            });
            logger.debug(join(subjects, "\n"));
            saveLastId();
        }
        return tweets;
    }

    private void saveLastId() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rootPath + File.separator + "lastid.txt")))) {
            writer.write(lastScanned + "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public List<Tweet> findRelated(Tweet tweet) {
        return findRelated(tweet, keywordsAnalyzer.findKeyWords(tweet));
    }

    private List<Tweet> findRelated(Tweet tweet, List<String> keyWords) {
        String keyWordsQuery = join(keyWords, " ");
        String since = " since:" + getSinceDate(tweet);
        String query = "#todayiam " + keyWordsQuery + since;
        logger.debug("query: " + query);
        List<Tweet> related = twitter.searchOperations().search(query).getTweets();
        return FluentIterable.from(related).filter(t -> t.getFromUserId() != tweet.getFromUserId()).toImmutableList();
    }
}
