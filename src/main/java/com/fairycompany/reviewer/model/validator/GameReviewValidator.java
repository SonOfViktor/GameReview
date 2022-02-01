package com.fairycompany.reviewer.model.validator;

import java.util.List;

/**
 * GameReview validator is used to validate request parameters for game review services.
 */
public class GameReviewValidator {
    private static final String TEXT_REVIEW_PATTERN = "[\\sА-Яа-яёЁ\\p{Graph}&&[^<>]]{0,1000}";
    private static final String RATING_REGEX = "0?\\d{1,2}";
    private static final String MAX_RATING = "100";

    private static GameReviewValidator instance = new GameReviewValidator();


    private GameReviewValidator() {
    }

    /**
     * Gets instance of GameReviewValidator.
     *
     * @return the instance of GameReviewValidator
     */
    public static GameReviewValidator getInstance() {
        return instance;
    }

    /**
     * Check whether game ratings can be parsed to int, and they are between 0 and 100.
     *
     * @param ratings the list of game ratings
     * @return {@code true} if every rating can be parsed to int, and they are between 0 and 100
     */
    public boolean isGameRatingValid(List<String> ratings) {
        return ratings != null && ratings.stream().allMatch(r -> r.matches(RATING_REGEX) || r.equals(MAX_RATING));
    }

    /**
     * Check whether text of review is valid.
     *
     * @param review the user review
     * @return {@code true} if review is valid
     */
    public boolean isReviewValid (String review) {
        return review != null && review.matches(TEXT_REVIEW_PATTERN);
    }
}
