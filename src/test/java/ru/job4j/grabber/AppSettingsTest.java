package ru.job4j.grabber;

import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

public class AppSettingsTest {

    @Test
    public void whenLoadProperties() {
        Properties ps = AppSettings.loadProperties();
        assertEquals("60", ps.getProperty("grabber.interval"));
        assertEquals("org.postgresql.Driver", ps.getProperty("jdbc.driver"));
        assertEquals("jdbc:postgresql://localhost:5432/grabber_db", ps.getProperty("jdbc.url"));
        assertEquals("root", ps.getProperty("jdbc.username"));
        assertEquals("qx8eec", ps.getProperty("jdbc.password"));
    }
}