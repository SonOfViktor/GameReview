package com.fairycompany.reviewer.model.validator;

import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Game validator is used to validate request parameters for game services.
 */
public class GameValidator {
    private static final String STRING_FIELD_REGEX = "[\\p{Alnum}][\\p{Alnum}\\s']{1,29}";
    private static final String SEARCH_FIELD_REGEX = "[\\p{Alnum}][\\p{Alnum}\\s']{0,29}";
    private static final String YOUTUBE_URL_REGEX = "https://www\\.youtube\\.com/watch\\?v=[\\w_-]{11}";
    private static final String DESCRIPTION_REGEX = "[\\sА-Яа-яёЁ\\p{Graph}&&[^<>]]{10,1000}";
    private static final String IMAGE_ADDRESS_REGEX = "media\\\\game\\\\[\\p{Alnum}][\\p{Alnum}\\s']{1,29}\\.\\p{Lower}{3,4}";
    private static final LocalDate LOW_LIMIT_DATE = LocalDate.of(2013, 11, 14);
    private static final String PRICE_REGEX = "\\d{1,3}(\\.\\d{1,2})?";
    private static final BigDecimal MIN_PRICE = new BigDecimal("0.01");
    private static final BigDecimal MAX_PRICE = new BigDecimal("200");
    private static final int MAX_PRICE_SCALE = 2;
    private static final int MIN_PLATFORM_NUMBER = 1;
    private static final int MAX_PLATFORM_NUMBER = 4;
    private static final int MIN_GENRES_NUMBER = 1;
    private static final int MAX_GENRES_NUMBER = 20;
    private static GameValidator instance = new GameValidator();


    private GameValidator() {
    }

    /**
     * Gets instance of GameValidator.
     *
     * @return the instance of GameValidator
     */
    public static GameValidator getInstance() {
        return instance;
    }

    /**
     * Check whether game data (name, publisher, developer) is valid.
     *
     * @param field the field with game data
     * @return {@code true} if data is valid
     */
    public boolean isDataFieldValid(String field) {
        return field != null && !field.isBlank() && field.matches(STRING_FIELD_REGEX);
    }

    /**
     * Check whether data in search field is valid.
     *
     * @param field string data in search field
     * @return {@code true} if search field is valid
     */
    public boolean isSearchFieldValid(String field) {
        return field != null && !field.isBlank() && field.matches(SEARCH_FIELD_REGEX);
    }

    /**
     * Check whether game release date is valid.
     *
     * @param releaseDate game release date
     * @return {@code true} if the date is between Playstation 4 release date (14.11.2013) and current day
     */
    public boolean isReleaseDateValid(LocalDate releaseDate) {
        LocalDate currentDate = LocalDate.now();
        return releaseDate != null &&
               (releaseDate.isAfter(LOW_LIMIT_DATE) && releaseDate.isBefore(currentDate) || releaseDate.isEqual(currentDate));
    }

    /**
     * Check whether price can be parsed to BigDecimal.
     *
     * @param price game price
     * @return {@code true} if price can be parsed to BigDecimal
     */
    public boolean isPriceBigDecimal(String price) {
        return price != null && price.matches(PRICE_REGEX);
    }

    /**
     * Check whether price is more than 0 and less than 200 and scale has 2 digits
     *
     * @param price the price
     * @return {@code true} if price is more than 0 and less than 200 and scale has 2 digits
     */
    public boolean isPriceValid(BigDecimal price) {
        return price != null && price.compareTo(MIN_PRICE) >= 0 &&
               price.compareTo(MAX_PRICE) <= 0 && price.scale() <= MAX_PRICE_SCALE;
    }

    /**
     * Check whether the specified platforms exist.
     *
     * @param platforms game platforms
     * @return {@code true} if the specified platforms exist
     */
    public boolean isPlatformsValid(String[] platforms) {
        return platforms != null &&
               platforms.length >= MIN_PLATFORM_NUMBER && platforms.length <= MAX_PLATFORM_NUMBER &&
               Arrays.stream(platforms).allMatch(this::isPlatformValid);
    }

    /**
     * Check whether the platform exists.
     *
     * @param platform game platform
     * @return {@code true} if the platform exists
     */
    public boolean isPlatformValid(String platform) {
        return Arrays.stream(Platform.values()).anyMatch((pl) -> pl.name().equals(platform));
    }

    /**
     * Check whether the specified genres exist.
     *
     * @param genres game genres
     * @return {@code true} if the specified genres exist
     */
    public boolean isGenresValid(String[] genres) {
        return genres != null &&
               genres.length >= MIN_GENRES_NUMBER && genres.length <= MAX_GENRES_NUMBER &&
               Arrays.stream(genres).allMatch(this::isGenreValid);
    }

    /**
     * Check whether the genre exists.
     *
     * @param genre game genre
     * @return {@code true} if the genre exists
     */
    public boolean isGenreValid(String genre) {
        return Arrays.stream(Game.Genre.values()).anyMatch((gnr) -> gnr.name().equals(genre));
    }

    /**
     * Check whether YouTube url is valid.
     *
     * @param url YouTube url
     * @return {@code true} if the url can be YouTube url
     */
    public boolean isYoutubeUrlValid(String url) {
        return url != null && url.matches(YOUTUBE_URL_REGEX);
    }

    /**
     * Check whether game description is valid.
     *
     * @param description game description
     * @return {@code true} if the description is valid
     */
    public boolean isDescriptionValid(String description) {
        return description != null && description.matches(DESCRIPTION_REGEX);
    }

    /**
     * Check whether image file address is valid.
     *
     * @param imageAddress the image file address
     * @return {@code true} if the image file address is valid
     */
    public boolean isImageAddressValid(String imageAddress) {
        return imageAddress != null && imageAddress.matches(IMAGE_ADDRESS_REGEX);
    }

    /**
     * Unites most method for game parameter validation.
     *
     * @param name        the name
     * @param publisher   the publisher
     * @param developer   the developer
     * @param platforms   the platforms
     * @param releaseDate the release date
     * @param price       the price
     * @param youtubeUrl  the youtube url
     * @param description the description
     * @return {@code true} if all parameters are valid
     */
    public boolean isAllGameDataValid(String name, String publisher, String developer, String[] platforms,
                                      LocalDate releaseDate, BigDecimal price, String youtubeUrl, String description) {
        boolean isValid = isDataFieldValid(name) && isDataFieldValid(publisher) &&
                          isDataFieldValid(developer) && isPlatformsValid(platforms) &&
                          isReleaseDateValid(releaseDate) && isPriceValid(price) &&
                          isYoutubeUrlValid(youtubeUrl) && isDescriptionValid(description);

        return isValid;
    }
}
