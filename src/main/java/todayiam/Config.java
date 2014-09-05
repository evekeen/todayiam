package todayiam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.io.FileNotFoundException;

/**
 * Author: Alexander Ivkin
 * Date: 03/09/14
 */
@Configuration
public class Config {

    @Value("consumerKey") private String consumerKey;
    @Value("consumerSecret") private String consumerSecret;
    @Value("accessToken") private String accessToken;
    @Value("accessTokenSecret") private String accessTokenSecret;

    @Bean public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() throws FileNotFoundException {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        FileSystemResource location = new FileSystemResource("todayiam.properties");
        if (!location.exists()) throw new FileNotFoundException("todayiam.properties not found in working directory: " + System.getProperty("user.dir"));
        propertyPlaceholderConfigurer.setLocation(location);
        return propertyPlaceholderConfigurer;
    }

    @Bean public Twitter twitter() {
        return new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
    }

    @Bean public Searcher searcher() {
        return new SearcherImpl(twitter(), keywordsAnalyzer());
    }

    @Bean public KeywordsAnalyzer keywordsAnalyzer() {
        KeywordsAnalyzerImpl keywordsAnalyzer = new KeywordsAnalyzerImpl();
        keywordsAnalyzer.setKeyWordNumber(2);
        return keywordsAnalyzer;
    }

    @Bean Replier replier() {
        return new ReplierImpl(twitter());
    }

    @Bean Scheduler scheduler() {
        return new Scheduler(searcher(), replier());
    }
}