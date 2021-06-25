package job4j.grabber.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class AlertRabbit {

    public static void main(String[] args) {
        try (InputStream in =
                AlertRabbit.class
                .getClassLoader()
                .getResourceAsStream("rabbit.properties")
        ) {
            Properties cfg = new Properties();
            cfg.load(in);
            int seconds = Integer.parseInt(cfg.getProperty("rabbit.interval", "15"));
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(seconds)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Ошибка чтения свойств из ресурса: " + ex);
            ex.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}
