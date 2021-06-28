package ru.job4j.grabber;

import ru.job4j.grabber.quartz.Grab;
import ru.job4j.grabber.quartz.Grabber;
import ru.job4j.grabber.repositories.PsqlPostStore;
import ru.job4j.grabber.repositories.Store;
import ru.job4j.grabber.web.PostService;

public class AppUI {

    public static void main(String[] args) {
        Store store = new PsqlPostStore();
        Grab grabber = new Grabber(store);
        grabber.runJobs();
        PostService httpService = new PostService(store);
        httpService.start();
    }
}
