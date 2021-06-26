package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppSettings {

    private static final Logger LOG = LoggerFactory.getLogger(AppSettings.class.getName());
    private static Properties settings;

    public static Properties loadProperties() {
        if (settings == null) {
            settings = new Properties();
            try (InputStream in =
                         AppUI.class
                                 .getClassLoader()
                                 .getResourceAsStream("rabbit.properties")
            ) {
                settings.load(in);
            } catch (IOException ex) {
                LOG.error("Ошибка загрузки свойств из ресурса!", ex);
            }
        }
        return settings;
    }
}
