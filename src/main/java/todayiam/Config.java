package todayiam;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

import java.io.IOException;

/**
 * Author: Alexander Ivkin
 * Date: 03/09/14
 */
@Configuration
public class Config {

    @Bean
    public Twitter twitter() {
        String consumerKey = "k4sT2zENvfAlqE4EY5LOz39uZ";
        String consumerSecret = "9g2hoe56ee5RQFMGm3258h4HB5ZYicBLZClgJ8hlPEyBjsyZJ4";
        return new TwitterTemplate(consumerKey, consumerSecret);
    }

    @Bean
    public Searcher searcher() throws IOException {
        return new Searcher(twitter());
    }
}