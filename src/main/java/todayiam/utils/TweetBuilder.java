package todayiam.utils;

import org.springframework.social.twitter.api.Tweet;

import java.util.Date;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public class TweetBuilder {
    private static long id = 0;
    private TweetTemplate template = new TweetTemplate();

    public TweetBuilder(String text) {
        withText(text);
        withId(id++);
    }

    public TweetBuilder withId(long id) {
        template.id = id;
        return this;
    }

    public TweetBuilder withFromUserId(long id) {
        template.from = id;
        return this;
    }

    public TweetBuilder withToUserId(long id) {
        template.to = id;
        return this;
    }

    public TweetBuilder withText(String text) {
        template.text = text;
        return this;
    }

    public TweetBuilder createdAt(Date date) {
        template.createdAt = date;
        return this;
    }

    public Tweet build() {
        return new Tweet(template.id, template.text, template.createdAt, template.fromUser, template.profileImageUrl, template.from, template.to, template.languageCode, template.source);
    }

    public static final class TweetTemplate {
        Date createdAt = new Date();
        long id = 1;
        String text = "some tweet text";
        long from = 1;
        long to = 2;
        String profileImageUrl = null;
        String fromUser = "test";
        String languageCode = null;
        String source = null;
    }
}
