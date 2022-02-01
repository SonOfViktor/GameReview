package com.fairycompany.reviewer.model.validator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class GameReviewValidatorTest {
    GameReviewValidator gameReviewValidator;

    @BeforeClass
    public void init(){
        gameReviewValidator = GameReviewValidator.getInstance();
    }

    @DataProvider(name = "gameRatingProvider")
    private Object[][] provideGameRating() {
        return new Object[][]{
                {List.of("100", "50", "9", "001"), true},
                {List.of("200", "50"), false},
                {List.of("101"), false},
                {List.of("-1"), false},
                {List.of("<script>alert(123)</script>"), false},
                {List.of(""), false},
                {null, false}
        };
    }

    @Test (dataProvider = "gameRatingProvider")
    public void testIsGameRatingValid(List<String> rating, boolean expected) {
        boolean actual = gameReviewValidator.isGameRatingValid(rating);

        assertEquals(actual, expected);
    }

    @Test
    public void testIsReviewValidPositive() {
        String review = """
                ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxy !"#$%&'()*+,-./:;=?@[\\]^_`{|}~
                АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ абвгдеёжзийклмнопрстуфхцчшщъыьэю
                """;

        assertTrue(gameReviewValidator.isReviewValid(review));
    }

    @Test
    public void testIsReviewValidNegative() {
        String review = """
                ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxy <a href="javascript:alert(123)">
                """;

        assertFalse(gameReviewValidator.isReviewValid(review));
    }


    @Test
    public void testIsReviewEmpty() {
        String review = "";

        assertTrue(gameReviewValidator.isReviewValid(review));
    }

    @Test
    public void testIsReviewNull() {
        assertFalse(gameReviewValidator.isReviewValid(null));
    }

}