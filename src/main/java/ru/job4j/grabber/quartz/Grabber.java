package ru.job4j.grabber.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.*;
import ru.job4j.grabber.html.Parse;
import ru.job4j.grabber.repositories.Store;

import java.util.HashMap;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {

    private static final Logger LOG = LoggerFactory.getLogger(Grabber.class.getName());

    private final Properties cfg;
    private final HashMap<String, Parse> parsers;

    public Grabber() {
        cfg = AppSettings.loadProperties();
        parsers = AppSettings.getParsers();
    }

    private Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    private void scheduleSqlRuJob(Scheduler scheduler, Store store) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parsers.get("sql.ru"));
        JobDetail job = newJob(GrabSqlRuJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("grabber.interval")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    @Override
    public void init(Store store) {
        try {
            Scheduler scheduler = scheduler();
            scheduleSqlRuJob(scheduler, store);
        } catch (SchedulerException ex) {
            LOG.error("Ошибка включения задания в расписание!", ex);
        }
    }
}