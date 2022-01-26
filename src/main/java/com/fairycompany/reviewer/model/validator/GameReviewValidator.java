package com.fairycompany.reviewer.model.validator;

import java.util.List;

public class GameReviewValidator {
    private static final String TEXT_REVIEW_PATTERN = "[\\sА-Яа-яёЁ\\p{Graph}&&[^<>]]{3,1000}";
    private static final String RATING_REGEX = "1?\\d{1,2}";

    private static GameReviewValidator instance = new GameReviewValidator();


    private GameReviewValidator() {
    }

    public static GameReviewValidator getInstance() {
        return instance;
    }

    public boolean isGameRatingValid(List<String> ratings) {
        return ratings.stream().allMatch(r -> r.matches(RATING_REGEX));
    }

    public boolean isReviewValid (String review) {
        return  review.isEmpty() || review.matches(TEXT_REVIEW_PATTERN);
    }
}
