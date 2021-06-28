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
import java.util.Set;

public class AppSettings {

    private static final Logger LOG = LoggerFactory.getLogger(AppSettings.class.getName());
    private static Properties settings;
    private static HashMap<String, Parse> parsers;

    private static void defineParsers() {
        parsers = new HashMap<>();
        //TODO добавить новые парсеры по мере их определения
        parsers.put("sql.ru", SqlRuParser.getInstance());
    }

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
     * Получить парсер для домена
     * @param domainName имя домена
     * @return Parse
     */

    public static Parse getParser(String domainName) {
        if (parsers == null) {
            defineParsers();
        }
        Parse result = parsers.get(domainName);
        if (result == null) {
            throw new IllegalArgumentException(
                "Парсер для домена " + domainName + " не определен!"
            );
        }
        return result;
    }

    /**
     * Получить домены, для которых определены парсеры
     * @return Множество доменных имен
     */

    public static Set<String> getDefinedDomains() {
        if (parsers == null) {
            defineParsers();
        }
        return parsers.keySet();
    }
}
