package com.fairycompany.reviewer.model.validator;

import java.util.List;

public class GameReviewValidator {
    private static final String TEXT_REVIEW_PATTERN = "[\\sА-Яа-яёЁ\\p{Graph}&&[^<>]]{10,1000}";
    private static final String TWO_DIGIT_INT_PATTERN = "\\d{1,2}";
    private static final String ONE_HUNDRED = "100";
    private static final int MIN_RATING = 0;
    private static final int MAX_RATING = 100;

    private static GameReviewValidator instance = new GameReviewValidator();


    private GameReviewValidator() {
    }

    public static GameReviewValidator getInstance() {
        return instance;
    }

    public boolean isGameRatingValid(List<String> ratings) {
        return ratings.stream().allMatch(r -> r.matches(TWO_DIGIT_INT_PATTERN) || r.equals(ONE_HUNDRED));
    }

    public boolean isReviewValid (String review) {
        return  review.isEmpty() || review.matches(TEXT_REVIEW_PATTERN);
    }
}
