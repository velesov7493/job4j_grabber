package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.html.Parse;
import ru.job4j.grabber.html.SqlRuParser;
import ru.job4j.grabber.quartz.Grabber;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class AppSettings {

    private static final Logger LOG = LoggerFactory.getLogger(AppSettings.class.getName());
    private static Properties settings;

    public static Properties loadProperties() {
        if (settings == null) {
            settings = new Properties();
            try (InputStream in =
                         Grabber.class
                                 .getClassLoader()
                                 .getResourceAsStream("grabber.properties")
            ) {
                settings.load(in);
            } catch (IOException ex) {
                LOG.error("Ошибка загрузки свойств из ресурса!", ex);
            }
        }
        return settings;
    }

    /**
     * Получить все активные парсеры системы
     * @return Карта<Домен,Парсер>
     */

    public static HashMap<String, Parse> getParsers() {
        HashMap<String, Parse> result = new HashMap<>();
        result.put("sql.ru", SqlRuParser.getInstance());
        return result;
    }
}
