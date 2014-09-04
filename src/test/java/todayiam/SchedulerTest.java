package todayiam;

import org.junit.Before;
import org.junit.Test;
import org.springframework.social.twitter.api.Tweet;
import todayiam.utils.TweetBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Author: Alexander Ivkin
 * Date: 04/09/14
 */
public class SchedulerTest {
    private Scheduler scheduler;
    private Searcher searcher = mock(Searcher.class);
    private Replier replier = mock(Replier.class);
    private Tweet orig = new TweetBuilder("orig").build();
    private List<Tweet> related = Arrays.asList(new TweetBuilder("related").build());

    @Before
    public void init() {
        scheduler = new Scheduler(searcher, replier);
        when(searcher.findNew()).thenReturn(Arrays.asList(orig));
        when(searcher.findRelated(orig)).thenReturn(related);
    }

    @Test
    public void replyWhenFound() throws Exception {
        scheduler.runOneIteration();
        verify(replier).reply(orig, related);
    }

    @Test
    public void noReplyWhenNotFound() throws Exception {
        when(searcher.findRelated(orig)).thenReturn(Collections.emptyList());
        scheduler.runOneIteration();
        verify(replier, never()).reply(any(Tweet.class), anyList());
    }
}
