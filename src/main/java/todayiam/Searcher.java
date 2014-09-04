package todayiam;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang3.StringUtils;
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

/**
 * Author: Alexander Ivkin
 * Date: 02/09/14
 */
public class Searcher {
    private Twitter twitter;
    private long lastScanned;
    private String rootPath = System.getProperty("user.home") + File.separator + ".todayiam";

    public Searcher(Twitter twitter) throws IOException {
        this.twitter = twitter;
        loadLastId();
    }

    private void loadLastId() throws IOException {
        File root = new File(rootPath);
        String fileName = rootPath + File.separator + "lastid.txt";
        if (!root.exists() || !root.isDirectory()) {
            root.mkdir();
            File lastIdFile = new File(fileName);
            lastIdFile.createNewFile();
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                lastScanned = Long.parseLong(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void findNew() {
        SearchParameters query = new SearchParameters("#todayiam").sinceId(lastScanned);
        List<Tweet> tweets = twitter.searchOperations().search(query).getTweets();
        final SimpleDateFormat format = new SimpleDateFormat("MM.dd.yyyy");
        if (tweets.size() > 0) {
            FluentIterable<String> subjects = FluentIterable.from(tweets).transform(new Function<Tweet, String>() {
                @Override
                public String apply(Tweet tweet) {
                    lastScanned = Math.max(lastScanned, tweet.getId());
                    return format.format(tweet.getCreatedAt()) + ": " +  tweet.getText();
                }
            });
            System.out.println(StringUtils.join(subjects, "\n"));
            saveLastId();
        }
    }

    private void saveLastId() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rootPath + File.separator + "lastid.txt")))) {
            writer.write(lastScanned + "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
