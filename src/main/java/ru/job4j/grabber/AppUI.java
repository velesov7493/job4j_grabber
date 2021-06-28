package ru.job4j.grabber;

import ru.job4j.grabber.quartz.Grab;
import ru.job4j.grabber.quartz.Grabber;
import ru.job4j.grabber.repositories.PsqlPostStore;
import ru.job4j.grabber.repositories.Store;

public class AppUI {

    public static void main(String[] args) {
        Grab grab = new Grabber();
        Store store = new PsqlPostStore();
        grab.init(store);
    }
}
