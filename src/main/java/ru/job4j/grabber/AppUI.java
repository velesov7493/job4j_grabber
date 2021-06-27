package ru.job4j.grabber;

import ru.job4j.grabber.html.SqlRuParser;
import ru.job4j.grabber.models.Post;
import ru.job4j.grabber.repositories.PsqlPostStore;

import java.util.ArrayList;
import java.util.List;

public class AppUI {

    public static void main(String[] args) {
        Parse parser = new SqlRuParser();
        Store store = new PsqlPostStore();
        List<Post> postList = parser.list("https://www.sql.ru/forum/job-offers");
        List<Post> details = new ArrayList<>();
        postList.forEach((p) -> details.add(parser.detail(p.getLink())));
        details.forEach(store::save);
        List<Post> storedPosts = store.getAll();
        System.out.println("Сохраненные вакансии:");
        storedPosts.forEach(System.out::println);
        System.out.println("Пост с id=8 :");
        System.out.println(store.findById(8));
    }
}
