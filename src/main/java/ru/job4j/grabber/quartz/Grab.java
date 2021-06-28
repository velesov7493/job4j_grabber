package ru.job4j.grabber.quartz;

import ru.job4j.grabber.repositories.Store;

public interface Grab {

    void init(Store store);
}
