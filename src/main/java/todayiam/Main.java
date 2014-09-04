package todayiam;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Author: Alexander Ivkin
 * Date: 03/09/14
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        Scheduler scheduler = context.getBean(Scheduler.class);
        scheduler.start();
    }
}
