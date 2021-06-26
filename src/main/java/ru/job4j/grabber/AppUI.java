package ru.job4j.grabber;

import ru.job4j.grabber.quartz.AlertRabbit;

import java.util.Properties;

public class AppUI {

    public static void main(String[] args) {
        Properties conf = AppSettings.loadProperties();
        int interval = Integer.parseInt(conf.getProperty("rabbit.interval"));
        AlertRabbit.run(interval);
    }
}
