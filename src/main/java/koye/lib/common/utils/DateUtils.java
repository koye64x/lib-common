package koye.lib.common.utils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    public static Date prevDay(Date date) {
        Instant prev = date.toInstant().minus(1, ChronoUnit.DAYS);
        return Date.from(prev);
    }

    public static LocalDate toLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date toDate(LocalDateTime localDateTime) {
        return java.util.Date
                .from(localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static Date startOfDay(Date date) {
        return toDate(toLocalDate(date).atStartOfDay());
    }

    public static Date endOfDay(Date date) {
        return toDate(toLocalDate(date).atTime(LocalTime.MAX));
    }

}
