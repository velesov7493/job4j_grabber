package ru.job4j.grabber.quartz;

import java.sql.*;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.database.DbUtils;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class AlertRabbit {

    private static final Logger LOG = LoggerFactory.getLogger(AlertRabbit.class.getName());

    public static void run(int interval) {
        try (Connection c = DbUtils.getConnection()) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connection", c);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                LOG.warn("Поток " + Thread.currentThread() + " преждевременно разбужен.");
            }
            scheduler.shutdown();
        } catch (SchedulerException ex) {
            LOG.error("Ошибка при создании расписания задачи!", ex);
        } catch (SQLException ex) {
            LOG.error("Ошибка выполнения запроса!", ex);
        }
    }

    public static class Rabbit implements Job {

        private void saveTimestamp(Connection c) {
            String query = "INSERT INTO tz_rabbits (created_date) VALUES (?);";
            try (PreparedStatement s = c.prepareStatement(query)) {
                s.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                s.executeUpdate();
            } catch (SQLException ex) {
                LOG.error("Ошибка при сохранении метки времени!", ex);
            }
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            saveTimestamp(cn);
        }
    }
}