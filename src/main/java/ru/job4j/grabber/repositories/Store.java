package ru.job4j.grabber.repositories;

import ru.job4j.grabber.models.Post;

import java.util.List;

public interface Store {

    boolean save(Post post);

    List<Post> getAll();

    Post findById(int id);
}
