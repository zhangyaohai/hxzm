package com.hxzm.common.kit;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by Pengrl on 2015/11/21.
 */
public class LocalDateTimeKit {

    public static final long getTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli();
    }

    public static final long getTime() {
        return getTime(LocalDateTime.now());
    }

    public static  final long getTime(LocalDate localDate) {
        return getTime(localDate.atStartOfDay());
    }

    public static final String now() {
        return LocalDateTime.now().format(UTCDATETIME_FORMATTER);
    }

    public static final String getUTCDate(java.util.Date date) {
        return getUTCDate(asLocalDate(date));
    }

    public static final String getUTCDate(LocalDate date) {
        return date.format(UTCDATE_FORMATTER);
    }

    public static final String getUTCDateTime(java.util.Date date) {
        return getUTCDateTime(asLocalDateTime(date));
    }

    public static final String getUTCDateTime(LocalDateTime dateTime) {
        return dateTime.format(UTCDATETIME_FORMATTER);
    }

    public static final String getPlainDateTime(LocalDateTime dateTime) {
        return dateTime.format(PLAINDATETIME_FORMATTER);
    }

    public static final String getPlainDate(LocalDate date) {
        return date.format(PLAINDATE_FORMATTER);
    }

    public static final String plusDays(long days) {
        return LocalDateTime.now().plusDays(days).format(UTCDATETIME_FORMATTER);
    }

    public static final String minusDays(long days) {
        return LocalDateTime.now().minusDays(days).format(UTCDATETIME_FORMATTER);
    }

    public static final String minusMinutes(int minutes) {
        return LocalDateTime.now().minusMinutes(minutes).format(UTCDATETIME_FORMATTER);
    }

    public static final String plusMinutes(long minutes) {
        return LocalDateTime.now().plusMinutes(minutes).format(UTCDATETIME_FORMATTER);
    }

    public static final String plusHours(long hours) {
        return LocalDateTime.now().plusHours(hours).format(UTCDATETIME_FORMATTER);
    }

    public static final String minusHours(long hours) {
        return LocalDateTime.now().minusHours(hours).format(UTCDATETIME_FORMATTER);
    }

    public static LocalDate asLocalDate(java.util.Date date) {
        return asLocalDate(date, ZoneId.systemDefault());
    }

    public static LocalDate asLocalDate(java.util.Date date, ZoneId zone) {
        if (date == null)
            return null;

        if (date instanceof java.sql.Date)
            return ((java.sql.Date) date).toLocalDate();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(java.util.Date date) {
        return asLocalDateTime(date, ZoneId.systemDefault());
    }

    public static LocalDateTime asLocalDateTime(java.util.Date date, ZoneId zone) {
        if (date == null)
            return null;

        if (date instanceof java.sql.Timestamp)
            return ((java.sql.Timestamp) date).toLocalDateTime();
        else
            return Instant.ofEpochMilli(date.getTime()).atZone(zone).toLocalDateTime();
    }

    public static java.util.Date asUtilDate(Object date) {
        return asUtilDate(date, ZoneId.systemDefault());
    }

    public static java.util.Date asUtilDate(Object date, ZoneId zone) {
        if (date == null)
            return null;

        if (date instanceof java.sql.Date || date instanceof java.sql.Timestamp)
            return new java.util.Date(((java.util.Date) date).getTime());
        if (date instanceof java.util.Date)
            return (java.util.Date) date;
        if (date instanceof LocalDate)
            return java.util.Date.from(((LocalDate) date).atStartOfDay(zone).toInstant());
        if (date instanceof LocalDateTime)
            return java.util.Date.from(((LocalDateTime) date).atZone(zone).toInstant());
        if (date instanceof ZonedDateTime)
            return java.util.Date.from(((ZonedDateTime) date).toInstant());
        if (date instanceof Instant)
            return java.util.Date.from((Instant) date);

        throw new UnsupportedOperationException("Don't know how to convert " + date.getClass().getName() + " to java.util.Date");
    }

    private static final DateTimeFormatter CNDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.CHINESE);
    private static final DateTimeFormatter CNTIME_FORMATTER = DateTimeFormatter.ofPattern("HH时mm分ss秒", Locale.CHINESE);
    public static final DateTimeFormatter CNDATETime_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINESE);

    public static final DateTimeFormatter UTCDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.CHINESE);
    public static final DateTimeFormatter UTCTIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.CHINESE);
    public static final DateTimeFormatter UTCDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);

    public static final DateTimeFormatter PLAINDATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.CHINESE);
    public static final DateTimeFormatter PLAINTIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss", Locale.CHINESE);
    public static final DateTimeFormatter PLAINDATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINESE);

    public static final DateTimeFormatter getDateTimeFormatter(String dateTimeFormatter) {
        return DateTimeFormatter.ofPattern(dateTimeFormatter, Locale.CHINESE);
    }
}
