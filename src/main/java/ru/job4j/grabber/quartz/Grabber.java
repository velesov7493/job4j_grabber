package ru.job4j.grabber.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.*;
import ru.job4j.grabber.html.Parse;
import ru.job4j.grabber.repositories.Store;

import java.util.Properties;
import java.util.Set;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {

    private static final Logger LOG = LoggerFactory.getLogger(Grabber.class.getName());

    private final Properties cfg;
    private final Store store;

    public Grabber(Store aStore) {
        cfg = AppSettings.loadProperties();
        store = aStore;
    }

    private Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    private void scheduleJob(String domainName, Scheduler scheduler) throws SchedulerException {
        Parse parser = AppSettings.getParser(domainName);
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parser);
        Class<? extends Job> jobClass;
        switch (domainName) {
            case "sql.ru": jobClass = GrabSqlRuJob.class; break;
            default: return;
        }
        JobDetail job = newJob(jobClass)
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
    public void runJobs() {
        try {
            Scheduler scheduler = scheduler();
            Set<String> domains = AppSettings.getDefinedDomains();
            for (String domain : domains) {
                scheduleJob(domain, scheduler);
            }
        } catch (SchedulerException ex) {
            LOG.error("Ошибка включения задания в расписание!", ex);
        }
    }
}