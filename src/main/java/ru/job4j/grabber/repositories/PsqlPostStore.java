package ru.job4j.grabber.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.Store;
import ru.job4j.grabber.database.DbUtils;
import ru.job4j.grabber.models.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PsqlPostStore implements Store, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlPostStore.class.getName());

    private Connection cnn;

    public PsqlPostStore() {
        cnn = DbUtils.getConnection();
    }

    @Override
    public void save(Post post) {
        String query =
                "INSERT INTO tz_posts (name, link, authorName, text, created)"
                + " VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement st = cnn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, post.getTitle());
            st.setString(2, post.getLink());
            st.setString(3, post.getAuthor());
            st.setString(4, post.getDescription());
            st.setTimestamp(5, Timestamp.valueOf(post.getCreated()));
            st.executeUpdate();
            ResultSet keys = st.getGeneratedKeys();
            if (keys.next()) {
                post.setId(keys.getInt(1));
            }
            keys.close();
        } catch (SQLException ex) {
            LOG.error("Ошибка записи вакансии!", ex);
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> result = new ArrayList<>();
        String query = "SELECT * FROM tz_posts;";
        try (PreparedStatement st = cnn.prepareStatement(query)) {
            ResultSet s = st.executeQuery();
            while (s.next()) {
                Post entry = new Post();
                entry.setId(s.getInt("id"));
                entry.setLink(s.getString("link"));
                entry.setTitle(s.getString("name"));
                entry.setAuthor(s.getString("authorName"));
                entry.setDescription(s.getString("text"));
                entry.setCreated(s.getTimestamp("created").toLocalDateTime());
                result.add(entry);
            }
            s.close();
        } catch (SQLException ex) {
            LOG.error("Ошибка получения списка вакансий!", ex);
        }
        return result;
    }

    @Override
    public Post findById(int id) {
        Post result = new Post();
        String query = "SELECT * FROM tz_posts WHERE id=?;";
        try (PreparedStatement st = cnn.prepareStatement(query)) {
            st.setInt(1, id);
            ResultSet s = st.executeQuery();
            if (s.next()) {
                result.setId(s.getInt("id"));
                result.setLink(s.getString("link"));
                result.setTitle(s.getString("name"));
                result.setAuthor(s.getString("authorName"));
                result.setDescription(s.getString("text"));
                result.setCreated(s.getTimestamp("created").toLocalDateTime());
            }
            s.close();
        } catch (SQLException ex) {
            LOG.error("Ошибка получения вакансии по id!", ex);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}
