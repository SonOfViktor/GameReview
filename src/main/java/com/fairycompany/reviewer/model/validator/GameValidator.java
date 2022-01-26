package com.fairycompany.reviewer.model.validator;

import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

public class GameValidator {
    private static final String STRING_FIELD_REGEX = "[\\p{Alnum}][\\p{Alnum}\\s']{1,29}";
    private static final String SEARCH_FIELD_REGEX = "[\\p{Alnum}][\\p{Alnum}\\s']{0,29}";
    private static final String YOUTUBE_URL_REGEX = "https://www\\.youtube\\.com/watch\\?v=[\\w_-]{11}";
    private static final String DESCRIPTION_REGEX = "[\\sА-Яа-яёЁ\\p{Graph}&&[^<>]]{10,1000}";
    private static final String IMAGE_ADDRESS_REGEX = "media\\game\\[\\p{Alnum}][\\p{Alnum}\\s']{1,29}";
    private static final LocalDate LOW_LIMIT_DATE = LocalDate.of(2013, 11, 14);
    private static final String PRICE_REGEX = "\\d{1,3}(\\.\\d{1,2})?";
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
        return field != null && !field.isBlank() && field.matches(STRING_FIELD_REGEX);
    }

    public boolean isSearchFieldValid(String field) {
        return field != null && !field.isBlank() && field.matches(SEARCH_FIELD_REGEX);
    }

    public boolean isReleaseDateValid(LocalDate releaseDate) {
        LocalDate currentDate = LocalDate.now();
        return releaseDate.isAfter(LOW_LIMIT_DATE) && releaseDate.isBefore(currentDate) || releaseDate.isEqual(currentDate);
    }

    public boolean isPriceBigDecimal(String price) {
        return price.matches(PRICE_REGEX);
    }

    public boolean isPriceValid(BigDecimal price) {
        return price.compareTo(MIN_PRICE) > 0 && price.compareTo(MAX_PRICE) <= 0 && price.scale() <= MAX_PRICE_SCALE;
    }

    public boolean isPlatformsValid(String[] platforms) {
        return platforms != null && Arrays.stream(platforms).allMatch(this::isPlatformValid);
    }

    public boolean isPlatformValid(String platform) {
        return Arrays.stream(Platform.values()).anyMatch((pl) -> pl.name().equals(platform));
    }

    public boolean isGenresValid(String[] genres) {
        return genres != null && Arrays.stream(genres).allMatch(this::isGenreValid);
    }

    public boolean isGenreValid(String genre) {
        return Arrays.stream(Game.Genre.values()).anyMatch((gnr) -> gnr.name().equals(genre));
    }

    public boolean isYoutubeUrlValid(String url) {
        return (url != null && url.matches(YOUTUBE_URL_REGEX));
    }

    public boolean isDescriptionValid(String description) {
        return description.matches(DESCRIPTION_REGEX);
    }

    public boolean isImageAddressValid(String imageAddress) {
        return imageAddress != null && imageAddress.matches(IMAGE_ADDRESS_REGEX);
    }

    public boolean isGameDataValid(String name, String publisher, String developer, String[] platforms,
                                   LocalDate releaseDate, BigDecimal price, String youtubeUrl, String description) {
        boolean isValid = isStringFieldValid(name) && isStringFieldValid(publisher) &&
                          isStringFieldValid(developer) && isPlatformsValid(platforms) &&
                          isReleaseDateValid(releaseDate) && isPriceValid(price) &&
                          isYoutubeUrlValid(youtubeUrl) && isDescriptionValid(description);

        return isValid;
    }
}
