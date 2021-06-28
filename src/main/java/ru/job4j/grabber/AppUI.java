package ru.job4j.grabber;

import ru.job4j.grabber.quartz.Grab;
import ru.job4j.grabber.quartz.Grabber;
import ru.job4j.grabber.repositories.PsqlPostStore;

public class AppUI {

    public static void main(String[] args) {
        Grab grabber = new Grabber(new PsqlPostStore());
        grabber.runJobs();
    }
}
