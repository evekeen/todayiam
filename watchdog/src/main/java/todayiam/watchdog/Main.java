package todayiam.watchdog;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author: Alexander Ivkin
 * Date: 03/09/14
 */
public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Scheduler scheduler = context.getBean(Scheduler.class);
        scheduler.startAsync();
        scheduler.addListener(new Service.Listener() {
            @Override public void starting() {
            }

            @Override public void running() {
            }

            @Override public void stopping(Service.State from) {
            }

            @Override public void terminated(Service.State from) {
            }

            @Override public void failed(Service.State from, Throwable failure) {
                logger.error("Error occurred in scheduler");
                throw new RuntimeException(failure);
            }
        }, MoreExecutors.directExecutor());
    }
}
