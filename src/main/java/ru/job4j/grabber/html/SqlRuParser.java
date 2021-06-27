package ru.job4j.grabber.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.models.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParser implements Parse {

    private static final Logger LOG = LoggerFactory.getLogger(SqlRuParser.class.getName());

    @Override
    public List<Post> list(String link) {
        List<Post> postList = new ArrayList<>();
        SqlRuDateTimeParser dtParser = new SqlRuDateTimeParser();
        try {
            Document doc = Jsoup.connect(link).get();
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
                    postList.add(p);
                }
            }
        } catch (IOException ex) {
            LOG.error("Ошибка чтения html-документа по адресу " + link, ex);
        }
        return postList;
    }

    @Override
    public Post detail(String link) {
        Post result = new Post();
        SqlRuDateTimeParser dtParser = new SqlRuDateTimeParser();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements tables = doc.select(".msgTable");
            Element titleTd = tables.get(0).child(0).child(0).child(0);
            Element msgTd = tables.get(0).child(0).child(1).child(1);
            Element authorTd = tables.get(0).child(0).child(1).child(0);
            Element msgFooterTd = tables.get(0).child(0).child(2).child(0);
            result.setTitle(titleTd.text());
            String[] parts = msgFooterTd.text().split(" \\[");
            result.setCreated(dtParser.parse(parts[0].trim()));
            result.setDescription(msgTd.text());
            result.setAuthor(authorTd.child(0).text());
            result.setLink(doc.location());
        } catch (IOException ex) {
            LOG.error("Ошибка получения деталей поста!", ex);
        }
        return result;
    }
}
