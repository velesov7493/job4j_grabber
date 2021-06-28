package ru.job4j.grabber.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.AppSettings;
import ru.job4j.grabber.models.Post;
import ru.job4j.grabber.repositories.Store;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class PostService extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(PostService.class.getName());

    private final Store store;
    private final int port;

    public PostService(Store aStore) {
        store = aStore;
        Properties cfg = AppSettings.loadProperties();
        port = Integer.parseInt(cfg.getProperty("http.port", "8080"));
        setPriority(Thread.MIN_PRIORITY);
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                try (OutputStream out = socket.getOutputStream()) {
                    out.write("HTTP/1.1 200 OK\r\n".getBytes());
                    out.write("Content-Type: text/plain; charset=utf-8\r\n\r\n".getBytes());
                    for (Post entry : store.getAll()) {
                        out.write(entry.toString().getBytes());
                        out.write(System.lineSeparator().getBytes());
                    }
                } catch (IOException ex) {
                    LOG.error("Ошибка ввода/вывода http-сервера!", ex);
                    server.close();
                }
            }
        } catch (Exception ex) {
            LOG.error("Ошибка http-сервера!", ex);
        }
    }
}
