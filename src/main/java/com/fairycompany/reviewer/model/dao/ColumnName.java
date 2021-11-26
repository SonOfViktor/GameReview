package com.fairycompany.reviewer.model.dao;

public final class ColumnName {

    // tables
    public static final String USERS = "users";
    public static final String ROLES = "roles";
    public static final String STATUSES = "statuses";
    public static final String GAMES = "games";
    public static final String GAMES_PLATFORMS = "games_platforms";
    public static final String PLATFORMS = "platforms";
    public static final String SCREENSHOTS = "screenshots";
    public static final String GENRES = "genres";
    public static final String GAMES_GENRES = "games_genres";
    public static final String GAME_RATING = "game_rating";
    public static final String LIKES = "likes";
    public static final String ORDERS = "orders";
    public static final String PAYMENTS = "payments";

    // users
    public static final String USER_ID = "user_id";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "first_name";
    public static final String SECOND_NAME = "second_name";
    public static final String BIRTHDAY_DATE = "birthday_date";
    public static final String PHONE = "phone";
    public static final String USER_RATING = "user_rating";
    public static final String BALANCE = "balance";
    public static final String PHOTO = "photo";
    public static final String USER_ROLE_ID = "role_id";
    public static final String USER_STATUS_ID = "status_id";
    public static final String FULL_NAME = "full_name";

    // roles
    public static final String ROLE_ID = "role_id";
    public static final String ROLE = "role";

    //statuses
    public static final String STATUS_ID = "status_id";
    public static final String STATUS = "status";

    // games
    public static final String GAME_ID = "game_id";
    public static final String NAME = "name";
    public static final String PUBLISHER = "publisher";
    public static final String DEVELOPER = "developer";
    public static final String RELEASE_DATE = "release_date";
    public static final String DESCRIPTION = "description";
    public static final String TOTAL_RATING = "total_rating";
    public static final String IMAGE = "image";
    public static final String TRAILER_URL = "trailer_url";
    public static final String PRICE = "price";
    public static final String PLATFORM = "platform";

    // screenshots
    public static final String SCREENSHOT_ID = "screenshot_id";
    public static final String SCREENSHOTS_GAME_ID = "game_id";
    public static final String SCREENSHOT = "screenshot";

    // genres
    public static final String GENRES_ID = "genres_id";
    public static final String GENRE = "genre";

    // games_genres
    public static final String GAMES_GENRES_GAME_ID = "game_id";
    public static final String GAMES_GENRES_GENRES_ID = "genres_id";

    // game_rating
    public static final String GAME_RATING_ID = "game_rating_id";
    public static final String GAME_RATING_USER_ID = "user_id";
    public static final String GAME_RATING_GAME_ID = "game_id";
    public static final String GAMEPLAY_RATING = "gameplay_rating";
    public static final String GRAPHICS_RATING = "graphics_rating";
    public static final String SOUND_RATING = "sound_rating";
    public static final String PLOT_RATING = "plot_rating";
    public static final String REVIEW = "review";
    public static final String USER_AMOUNT = "user_amount";

    // likes
    public static final String LIKES_USER_ID = "user_id";
    public static final String LIKES_GAME_RATING_ID = "game_rating_id";
    public static final String LIKE = "like";

    // orders
    public static final String ORDER_ID = "order_id";
    public static final String ORDER_GAME_ID = "game_id";
    public static final String ORDER_PAYMENT_ID = "payment_id";
    public static final String ORDER_PLATFORM = "platform";

    // payments
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_USER_ID = "user_id";
    public static final String PAYMENT_DATE = "payment_date";

    // tokens
    public static final String TOKEN_ID = "token_id";
    public static final String TOKEN = "token";
    public static final String TOKEN_CREATION_DATE = "create_date";

    public static final String TOTAL_VALUE = "total_value";

    private ColumnName(){
    }
}
