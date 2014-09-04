package todayiam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

/**
 * Author: Alexander Ivkin
 * Date: 03/09/14
 */
@Configuration
public class Config {

    @Bean public Twitter twitter() {
        String consumerKey = "k4sT2zENvfAlqE4EY5LOz39uZ";
        String consumerSecret = "9g2hoe56ee5RQFMGm3258h4HB5ZYicBLZClgJ8hlPEyBjsyZJ4";
        String accessToken = "2788135837-Y9ItYg5MvOWc7bXASEeNgGmObvL1TYqcYFFqFDX";
        String accessTokenSecret = "uxMj6EiN7S4bltzo9uSqvg2lK3MJ9MZecva8VsLdgEWUE";
        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    @Bean public Searcher searcher() {
        return new SearcherImpl(twitter(), keywordsAnalyzer());
    }

    @Bean public KeywordsAnalyzer keywordsAnalyzer() {
        return new KeywordsAnalyzerImpl();
    }

    @Bean Replier replier() {
        return new ReplierImpl(twitter());
    }

    @Bean Scheduler scheduler() {
        return new Scheduler(searcher(), replier());
    }
}