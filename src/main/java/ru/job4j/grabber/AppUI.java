package ru.job4j.grabber;

import ru.job4j.grabber.html.SqlRuParser;

public class AppUI {

    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) {
            String page = i == 1 ? "" : "/" + i;
            SqlRuParser.parse("https://www.sql.ru/forum/job-offers" + page);
        }
    }
}
