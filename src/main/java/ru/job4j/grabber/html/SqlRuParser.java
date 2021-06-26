package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SqlRuParser {

    private static final Logger LOG = LoggerFactory.getLogger(SqlRuParser.class.getName());

    public static void parse(String uri) {
        try {
            Document doc = Jsoup.connect(uri).get();
            Elements tables = doc.select(".forumTable");
            Elements rows = tables.get(0).child(0).getElementsByTag("tr");
            for (Element tr : rows) {
                for (Element td : tr.getElementsByTag("td")) {
                    if (td.hasClass("postslisttopic")) {
                        Element href = td.child(0);
                        System.out.println(href.attr("href"));
                        System.out.println(href.text());
                    } else if (td.hasClass("altCol")) {
                        Elements tdChilds = td.children();
                        if (tdChilds.size() == 0) {
                            System.out.printf(
                                    "Дата публикации: %s"
                                    + System.lineSeparator(), td.text()
                            );
                        } else {
                            System.out.printf(
                                    "Автор: %s"
                                    + System.lineSeparator(), tdChilds.get(0).text()
                            );
                        }
                    }
                }
                System.out.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
            }
        } catch (IOException ex) {
            LOG.error("Ошибка чтения html-документа по адресу " + uri, ex);
        }
    }
}