package com.fairycompany.reviewer.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServiceUtil {
    private static final LocalDate DEFAULT_DATE = LocalDate.of(1, 1, 1);
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
    private static final ServiceUtil instance = new ServiceUtil();

    private ServiceUtil() {
    }

    public static ServiceUtil getInstance() {
        return instance;
    }

    public LocalDate getDateFromString(String stringDate) {
        return (stringDate != null && stringDate.matches(DATE_PATTERN)) ?
                LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)) :
                DEFAULT_DATE;
    }

}
