package com.fairycompany.reviewer.model.validator;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GameValidator {
    private static final String STRING_FIELD_PATTERN = "[\\p{Alnum}][\\p{Alnum}\\s']{1,29}";
    private static final String YOUTUBE_URL_PATTERN = "https://www\\.youtube\\.com/watch\\?v=[\\w_-]{10,20}";
    private static final String DESCRIPTION_PATTERN = "[\\sА-Яа-яёЁ\\p{Graph}&&[^<>]]{10,1000}";
    private static final LocalDate LOW_LIMIT_DATE = LocalDate.of(2013, 11, 14);
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");
    private static final BigDecimal MAX_PRICE = new BigDecimal("200");
    private static final int MAX_PRICE_SCALE = 2;
    private static GameValidator instance = new GameValidator();


    private GameValidator() {
    }

    public static GameValidator getInstance() {
        return instance;
    }

    public boolean isStringFieldValid(String field) {
        return field != null && !field.isBlank() && field.matches(STRING_FIELD_PATTERN);
    }

    public boolean isReleaseDateValid(LocalDate releaseDate) {
        LocalDate currentDate = LocalDate.now();
        return releaseDate.isAfter(LOW_LIMIT_DATE) && releaseDate.isBefore(currentDate) || releaseDate.isEqual(currentDate);
    }

    public boolean isPriceValid(BigDecimal price) {
        return price.compareTo(MIN_PRICE) > 0 && price.compareTo(MAX_PRICE) <= 0 && price.scale() <= MAX_PRICE_SCALE;
    }

    public boolean isCheckBoxDataValid(String[] data) {
        return data.length > 0;
    }

    public boolean isYoutubeUrlValid(String ulr) {
        return ulr.matches(YOUTUBE_URL_PATTERN);
    }

    public boolean isDescriptionValid(String description) {
        return description.matches(DESCRIPTION_PATTERN);
    }
}
