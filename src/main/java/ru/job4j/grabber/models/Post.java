package ru.job4j.grabber.models;

import java.time.LocalDateTime;

public class Post {

    private int id;
    private String title;
    private String link;
    private String author;
    private String description;
    private LocalDateTime created;

    public Post() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        String ls = System.lineSeparator();
        return
                "Post{" + ls
                + "\tid=" + id + ls
                + "\ttitle='" + title + '\'' + ls
                + "\tauthor='" + author + '\'' + ls
                + "\tlink='" + link + '\'' + ls
                + "\tdescription='" + description + '\'' + ls
                + "\tcreated=" + created + ls
                + '}';
    }
}