package ru.job4j.grabber.repositories;

import ru.job4j.grabber.Store;
import ru.job4j.grabber.models.Post;

import java.util.List;

public class DbPostStore implements Store {

    @Override
    public void save(Post post) {

    }

    @Override
    public List<Post> getAll() {
        return null;
    }

    @Override
    public Post findById(int id) {
        return null;
    }
}
