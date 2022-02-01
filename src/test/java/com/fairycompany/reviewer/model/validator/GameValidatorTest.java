package com.fairycompany.reviewer.model.validator;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.testng.Assert.*;

public class GameValidatorTest {
    private static final String IMAGE_PATH = "media\\game\\";
    GameValidator gameValidator;

    @BeforeClass
    public void init(){
        gameValidator = GameValidator.getInstance();
    }

    @DataProvider(name = "forDataFieldCheck")
    private Object[][] dataForDataField() {
        return new Object[][]{
                {"Assassin's Creed 2", true},
                {"Fe", true},
                {"Default game", true},
                {"South Park Fractured But Whole", true},
                {"R", false},
                {"This Field Exceeds a Number of Symbols", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider(name = "forSearchFieldCheck")
    private Object[][] dataForSearchField() {
        return new Object[][]{
                {"Assassin's Creed 2", true},
                {"South Park Fractured But Whole", true},
                {"R", true},
                {"This Field Exceeds a Number of Symbols", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider(name = "forReleaseDateCheck")
    private Object[][] dataForReleaseDate() {
        return new Object[][]{
                {LocalDate.now(), true},
                {LocalDate.of(2013, 11, 15), true},
                {LocalDate.now().plusDays(1), false},
                {LocalDate.of(2013, 11, 14), false},
                {null, false}
        };
    }

    @DataProvider(name = "forStringPriceCheck")
    private Object[][] dataForStringPrice() {
        return new Object[][]{
                {"999.99", true},
                {"999", true},
                {"0.01", true},
                {"0", true},
                {"1000", false},
                {"0.011", false},
                {"5.", false},
                {"-23.34", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider(name = "forPriceCheck")
    private Object[][] dataForPrice() {
        return new Object[][]{
                {new BigDecimal("200"), true},
                {new BigDecimal("23.34"), true},
                {new BigDecimal("0.01"), true},
                {new BigDecimal("200.01"), false},
                {new BigDecimal("0"), false},
                {new BigDecimal("-23.34"), false},
                {new BigDecimal("2.334"), false},
                {null, false}
        };
    }

    @DataProvider(name = "forPlatformCheck")
    private Object[][] dataForPlatform() {
        return new Object[][]{
                {"PLAYSTATION_4", true},
                {"XBOX_ONE", true},
                {"NINTENDO_SWITCH", true},
                {"PC", true},
                {"xbox_one", false},
                {"PLAYSTATION 4", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider(name = "forGenreCheck")
    private Object[][] dataForGenre() {
        return new Object[][]{
                {"ACTION", true},
                {"STRATEGY", true},
                {"THIRD_PERSON", true},
                {"THIRD PERSON", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider(name = "forYoutubeCheck")
    private Object[][] dataForYoutube() {
        return new Object[][]{
                {"https://www.youtube.com/watch?v=ss_Ncw-ALS4", true},
                {"https://www.youtube.com/watch?v=ss3NcwRALS4", true},
                {"https://www.youtube.com/watch?v=ss3NcDwRALS4", false},
                {"https://www.youtube.com/watch?v=ss3Nc[RALS4", false},
                {"https://www.youtube.com/watchv=ss3NceRALS4", false},
                {"http://www.youtube.com/watch?v=ss3NceRALS4", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @Test (dataProvider = "forDataFieldCheck")
    public void testIsStringFieldValid(String data, boolean expected) {
        boolean actual = gameValidator.isDataFieldValid(data);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forSearchFieldCheck")
    public void testIsSearchFieldValid(String data, boolean expected) {
        boolean actual = gameValidator.isSearchFieldValid(data);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forReleaseDateCheck")
    public void testIsReleaseDateValid(LocalDate releaseDate, boolean expected) {
        boolean actual = gameValidator.isReleaseDateValid(releaseDate);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forStringPriceCheck")
    public void testIsPriceBigDecimal(String price, boolean expected) {
        boolean actual = gameValidator.isPriceBigDecimal(price);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forPriceCheck")
    public void testIsPriceValid(BigDecimal price, boolean expected) {
        boolean actual = gameValidator.isPriceValid(price);

        assertEquals(actual, expected);
    }

    @Test
    public void testIsPlatformsValidPositive() {
        String[] platforms = {"PLAYSTATION_4", "XBOX_ONE", "NINTENDO_SWITCH", "PC"};

        assertTrue(gameValidator.isPlatformsValid(platforms));
    }

    @Test
    public void testIsPlatformsValidNegative() {
        String[] platforms = {"playstation4", "", "<script>alert(123)</script>", null};

        assertFalse(gameValidator.isPlatformsValid(platforms));
    }

    @Test
    public void testIsPlatformsEmpty() {
        String[] platforms = {};

        assertFalse(gameValidator.isPlatformsValid(platforms));
    }

    @Test
    public void testIsPlatformsExceed() {
        String[] platforms = {"PLAYSTATION_4", "XBOX_ONE", "NINTENDO_SWITCH", "PC", "XBOX_ONE"};

        assertFalse(gameValidator.isPlatformsValid(platforms));
    }

    @Test
    public void testIsPlatformsNull() {
        assertFalse(gameValidator.isPlatformsValid(null));
    }

    @Test (dataProvider = "forPlatformCheck")
    public void testIsPlatformValid(String platform, boolean expected) {
        boolean actual = gameValidator.isPlatformValid(platform);

        assertEquals(actual, expected);
    }

    @Test
    public void testIsGenresValidPositive() {
        String[] genres = {"ACTION", "SHOOTER", "STRATEGY", "PLATFORMER", "FIGHTING", "RACING", "RPG", "HORROR",
                           "SLASHER", "MMO", "ADVENTURE", "QUEST", "STEALTH", "TOP_DOWN", "THIRD_PERSON",
                           "FIRST_PERSON", "TWO_DIMENSIONAL", "METROIDVANIA", "SOULS_LIKE", "OPEN_WORLD"};

        assertTrue(gameValidator.isGenresValid(genres));
    }

    @Test
    public void testIsGenresValidNegative() {
        String[] genres = {"shooter", "", "<script>alert(123)</script>", null};

        assertFalse(gameValidator.isGenresValid(genres));
    }

    @Test
    public void testIsGenresEmpty() {
        String[] genres = {};

        assertFalse(gameValidator.isGenresValid(genres));
    }

    @Test
    public void testIsGenresExceed() {
        String[] genres = {"ACTION", "SHOOTER", "STRATEGY", "PLATFORMER", "FIGHTING", "RACING", "RPG", "HORROR",
                           "SLASHER", "MMO", "ADVENTURE", "QUEST", "STEALTH", "TOP_DOWN", "THIRD_PERSON",
                           "FIRST_PERSON", "TWO_DIMENSIONAL", "METROIDVANIA", "SOULS_LIKE", "OPEN_WORLD", "MMO"};

        assertFalse(gameValidator.isGenresValid(genres));
    }

    @Test
    public void testIsGenresNull() {
        assertFalse(gameValidator.isPlatformsValid(null));
    }

    @Test (dataProvider = "forGenreCheck")
    public void testIsGenreValid(String genre, boolean expected) {
        boolean actual = gameValidator.isGenreValid(genre);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forYoutubeCheck")
    public void testIsYoutubeUrlValid(String youtubeUrl, boolean expected) {
        boolean actual = gameValidator.isYoutubeUrlValid(youtubeUrl);

        assertEquals(actual, expected);
    }

    @Test
    public void testIsDescriptionValidPositive() {
        String description = """
                ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxy !"#$%&'()*+,-./:;=?@[\\]^_`{|}~
                АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ абвгдеёжзийклмнопрстуфхцчшщъыьэю
                """;

        assertTrue(gameValidator.isDescriptionValid(description));
    }

    @Test
    public void testIsDescriptionValidNegative() {
        String description = """
                ABCDEFGHIJKLMNOPQRSTUVWXYZ <a href="javascript:alert(123)"> abcdefghijklmnopqrstuvwxy
                """;

        assertFalse(gameValidator.isDescriptionValid(description));
    }

    @Test
    public void testIsDescriptionSymbolLack() {
        String description = "AdC1EFG!";

        assertFalse(gameValidator.isDescriptionValid(description));
    }

    @Test
    public void testIsDescriptionValidNull() {

        assertFalse(gameValidator.isDescriptionValid(null));
    }

    @Test (dataProvider = "forDataFieldCheck")
    public void testIsImageAddressValid(String imageName, boolean expected) {
        boolean actual = imageName != null && gameValidator.isImageAddressValid(IMAGE_PATH + imageName + ".jpg");

        assertEquals(actual, expected);
    }

    @Test
    public void testIsImageAddressNull() {
        assertFalse(gameValidator.isImageAddressValid(null));
    }

    @Test
    public void testIsGameDataValid() {
        String name = "Game";
        String publisher = "Publisher";
        String developer = "Developer";
        String[] platforms = {"PC", "XBOX_ONE"};
        LocalDate releaseDate = LocalDate.now();
        BigDecimal price = new BigDecimal("9.99");
        String youtubeUrl = "https://www.youtube.com/watch?v=ss_Ncw-ALS4";
        String description = "Really interesting игра!!!";

        boolean actual = gameValidator.isAllGameDataValid(name, publisher, developer, platforms,
                                                            releaseDate, price, youtubeUrl, description);

        assertTrue(actual);
    }
}