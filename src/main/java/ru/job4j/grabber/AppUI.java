package ru.job4j.grabber;

import ru.job4j.grabber.html.Parse;
import ru.job4j.grabber.html.SqlRuParser;
import ru.job4j.grabber.models.Post;

import java.util.ArrayList;
import java.util.List;

public class AppUI {

    public static void main(String[] args) {
        Parse parser = new SqlRuParser();
        List<Post> postList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String page = i == 1 ? "" : "/" + i;
            postList.addAll(parser.list("https://www.sql.ru/forum/job-offers" + page));
        }
        Post details = parser.detail(postList.get(8).getLink());
        postList.forEach(System.out::println);
        System.out.println("Детали поста 8");
        System.out.println(details);
    }
}
