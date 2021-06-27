package ru.job4j.grabber.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static class SqlRuDateFormatSymbols extends DateFormatSymbols {

        @Override
        public String[] getShortMonths() {
            return new String[] {
                    "янв", "фев", "мар", "апр", "май", "июн",
                    "июл", "авг", "сен", "окт", "ноя", "дек"
            };
        }
    }

    private Date yesterday() {
        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.DATE, -1);
        return cl.getTime();
    }

    private Date changeTime(Date src, Date newTime) {
        Calendar cl1 = Calendar.getInstance();
        Calendar cl2 = Calendar.getInstance();
        cl1.setTime(src);
        cl2.setTime(newTime);
        cl1.set(Calendar.HOUR_OF_DAY, cl2.get(Calendar.HOUR_OF_DAY));
        cl1.set(Calendar.MINUTE, cl2.get(Calendar.MINUTE));
        cl1.set(Calendar.SECOND, 0);
        cl1.set(Calendar.MILLISECOND, 0);
        return cl1.getTime();
    }

    private LocalDateTime dateToLocalDateTime(Date in) {
        return LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
    }

    @Override
    public LocalDateTime parse(String data) {
        SimpleDateFormat f1 = new SimpleDateFormat("d MMM yy, HH:mm", new SqlRuDateFormatSymbols());
        SimpleDateFormat f2 = new SimpleDateFormat("HH:mm");
        String[] parts = data.split(", ");
        Date result = new Date();
        try {
            if ("сегодня".equals(parts[0])) {
                result = new Date();
                Date time = f2.parse(parts[1]);
                result = changeTime(result, time);
            } else if ("вчера".equals(parts[0])) {
                result = yesterday();
                Date time = f2.parse(parts[1]);
                result = changeTime(result, time);
            } else {
                result = f1.parse(data);
            }
        } catch (ParseException ex) {
            System.out.println("Ошибка разбора даты: " + ex);
        }
        return dateToLocalDateTime(result);
    }

}
