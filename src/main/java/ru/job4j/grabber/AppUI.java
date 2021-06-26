package ru.job4j.grabber;

import ru.job4j.grabber.html.SqlRuParser;

public class AppUI {

    public static void main(String[] args) {
        SqlRuParser.parse("https://www.sql.ru/forum/job-offers");
    }
}
