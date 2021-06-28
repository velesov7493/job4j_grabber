package ru.job4j.grabber.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.grabber.html.Parse;
import ru.job4j.grabber.repositories.Store;
import ru.job4j.grabber.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GrabSqlRuJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(GrabSqlRuJob.class.getName());

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getJobDetail().getJobDataMap();
        Store store = (Store) map.get("store");
        Parse parse = (Parse) map.get("parse");
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String page = i == 1 ? "" : "/" + i;
            posts.addAll(parse.list("https://www.sql.ru/forum/job-offers" + page));
        }
        Pattern ptJava = Pattern.compile("^.*\\bjava\\b.*$");
        List<Post> javaPosts =
                posts.stream()
                .filter((p) -> {
                    Matcher m = ptJava.matcher(p.getTitle().toLowerCase());
                    return m.matches();
                })
                .map((p) -> parse.detail(p.getLink()))
                .collect(Collectors.toList());
        int newPostsCount = 0;
        for (Post entry : javaPosts) {
            newPostsCount += store.save(entry) ? 1 : 0;
        }
        LOG.info(
                "На sql.ru найдено " + javaPosts.size()
                + " java-вакансий. Из них новых: " + newPostsCount
        );
    }
}
