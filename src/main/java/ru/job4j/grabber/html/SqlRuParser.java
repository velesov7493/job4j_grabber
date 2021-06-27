package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.models.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParser {

    private static final Logger LOG = LoggerFactory.getLogger(SqlRuParser.class.getName());

    private static void addPostDescription(Post in) {
        try {
           Document doc = Jsoup.connect(in.getLink()).get();
           Elements tables = doc.select(".msgTable");
           Element topTd = tables.get(0).child(0).child(1).child(1);
           in.setDescription(topTd.text());
       } catch (IOException ex) {
           LOG.error("Ошибка получения деталей поста!", ex);
       }
    }

    public static void parse(String uri) {
        List<Post> postList = new ArrayList<>();
        SqlRuDateTimeParser dtParser = new SqlRuDateTimeParser();
        try {
            Document doc = Jsoup.connect(uri).get();
            Elements tables = doc.select(".forumTable");
            Elements rows = tables.get(0).child(0).getElementsByTag("tr");
            for (Element tr : rows) {
                Post p = new Post();
                boolean modified = false;
                for (Element td : tr.getElementsByTag("td")) {
                    if (td.hasClass("postslisttopic")) {
                        Element href = td.child(0);
                        p.setLink(href.attr("href"));
                        p.setTitle(href.text());
                        modified = true;
                    } else if (td.hasClass("altCol")) {
                        Elements tdChilds = td.children();
                        if (tdChilds.size() == 0) {
                            p.setCreated(dtParser.parse(td.text()));
                        } else {
                            p.setAuthor(tdChilds.get(0).text());
                        }
                        modified = true;
                    }
                }
                if (modified) {
                    addPostDescription(p);
                    postList.add(p);
                }
            }
        } catch (IOException ex) {
            LOG.error("Ошибка чтения html-документа по адресу " + uri, ex);
        }
        postList.forEach(System.out::println);
    }
}
