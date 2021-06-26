package ru.job4j.grabber.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.AppSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbUtils {

    private static final Logger LOG = LoggerFactory.getLogger(DbUtils.class.getName());
    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Properties cfg = AppSettings.loadProperties();
                Class.forName(cfg.getProperty("jdbc.driver"));
                connection = DriverManager.getConnection(
                        cfg.getProperty("jdbc.url"),
                        cfg.getProperty("jdbc.username"),
                        cfg.getProperty("jdbc.password")
                );
            }
        } catch (ClassNotFoundException ex) {
            LOG.error("Класс драйвера БД не найден!", ex);
            LOG.info("Выключаюсь...");
            System.exit(2);
        } catch (SQLException ex) {
            LOG.error("Ошибка создания подключения к БД!", ex);
            LOG.info("Выключаюсь...");
            System.exit(2);
        }
        return connection;
    }
}
