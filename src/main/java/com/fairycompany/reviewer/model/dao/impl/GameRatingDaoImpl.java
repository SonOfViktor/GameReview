package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.GameRatingDao;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.mapper.impl.GameRatingResultSetHandler;
import com.fairycompany.reviewer.model.entity.GameRating;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class GameRatingDaoImpl implements GameRatingDao {
    private static final Logger logger = LogManager.getLogger();
    private static GameRatingDaoImpl instance = new GameRatingDaoImpl();

    private static final String FIND_BY_GAME_AND_USER_ID_SQL = """
            SELECT game_rating_id, user_id, game_id, gameplay_rating, graphics_rating, sound_rating, plot_rating, review, publication_date
            FROM game_rating
            WHERE user_id = ? AND game_id = ?
            """;

    private static final String FIND_USER_AMOUNT_SQL = """
            SELECT COUNT(user_id) AS user_amount
            FROM game_rating
            GROUP BY game_id
            HAVING game_id = ?
            """;

    private static final String FIND_TOTAL_GAME_RATING_SQL = """
            SELECT total_rating FROM total_game_rating
            WHERE game_id = ?
            """;

    private static final String FIND_ALL_REVIEW_FOR_GAME_SQL = """
            SELECT game_rating_id, review, CONCAT(first_name, ' ', second_name) AS full_name, publication_date
            FROM game_rating.game_rating
            JOIN users ON users.user_id = game_rating.user_id
            WHERE game_id = ? AND users.user_id <> ? AND TRIM(review) <> ''
            LIMIT ?, ?
            """;

    private static final String FIND_TOTAL_REVIEW_AMOUNT_SQL = """
            SELECT COUNT(*) AS total_value FROM game_rating
            WHERE game_id = ? AND user_id <> ? AND review <> ''
            """;

    private static final String COUNT_USER_GAME_RATING_AMOUNT_SQL = """
            SELECT count(game_id) AS amount_game_rating, count(if(review <> '', 1, NULL)) AS amount_review,
            	count(if(user_rating BETWEEN 0 AND 29, 1, NULL)) AS negative_amount,
            	count(if(user_rating BETWEEN 30 AND 74, 1, NULL)) AS mixed_amount,
            	count(if(user_rating BETWEEN 75 AND 100, 1, NULL)) AS positive_amount
            FROM total_user_rating
            WHERE user_id = ?
            """;

    private static final String FIND_USER_MIN_MAX_GAME_RATING_SQL = """
            SELECT max_game_name, max_rating, min_game_name, min_rating FROM min_max_game_rating
            WHERE user_id = ? LIMIT 1
            """;

    private static final String ADD_NEW_GAME_RATING_SQL = """
            INSERT INTO game_rating (user_id, game_id, gameplay_rating, graphics_rating, sound_rating, plot_rating, review)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_GAME_RATING_SQL = """
            UPDATE game_rating
            SET gameplay_rating = ?, graphics_rating = ?, sound_rating = ?, plot_rating = ?, review = ?, publication_date = CURRENT_TIMESTAMP
            WHERE user_id = ? AND game_id = ?
            """;

    private static final String DELETE_RATING_SQL = """          
            DELETE FROM game_rating
            WHERE user_id = ? AND game_id = ?
            """;

    private static final String DELETE_REVIEW_SQL = """
            UPDATE game_rating
            SET review = ""
            WHERE game_rating_id = ?
            """;

    private JdbcTemplate<GameRating> jdbcTemplate;

    private GameRatingDaoImpl() {
        jdbcTemplate = new JdbcTemplate<>(new GameRatingResultSetHandler());
    }

    public static GameRatingDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<GameRating> findAll(long skippedUsers, int rowAmount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<GameRating> findEntityById(long gameRatingId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<GameRating> findGameRatingById(long userId, long gameId) throws DaoException {
        Optional<GameRating> rating = jdbcTemplate.executeSelectQueryForObject(FIND_BY_GAME_AND_USER_ID_SQL, userId, gameId);

        return rating;
    }

    @Override
    public long findUserAmount(long gameId) throws DaoException {
        Number userAmount = jdbcTemplate.executeSelectCalculation(FIND_USER_AMOUNT_SQL, USER_AMOUNT, gameId);

        return (userAmount != null) ? userAmount.longValue() : 0;
    }

    @Override
    public List<Map<String, Object>> findReviewsForGame(long gameId, long userId, long skippedRows, int rowAmount) throws DaoException {
        Set<String> columnNames = Set.of(GAME_RATING_ID, FULL_NAME, REVIEW, PUBLICATION_DATE);
        List<Map<String, Object>> reviewsForGame = jdbcTemplate
                .executeSelectSomeFields(FIND_ALL_REVIEW_FOR_GAME_SQL, columnNames, gameId, userId, skippedRows, rowAmount);

        return reviewsForGame;
    }

    @Override
    public long findTotalGameRatingReview(long gameId, long userId) throws DaoException {
        Number totalGameRatingReviewAmount = jdbcTemplate.executeSelectCalculation(FIND_TOTAL_REVIEW_AMOUNT_SQL, TOTAL_VALUE, gameId, userId);
        logger.log(Level.DEBUG, "Total game amount is {}", totalGameRatingReviewAmount);

        return (totalGameRatingReviewAmount != null) ? totalGameRatingReviewAmount.longValue() : 0;
    }

    @Override
    public List<Map<String, Object>> findRatingAmount(long userId) throws DaoException {
        Set<String> columnNames = Set.of(AMOUNT_GAME_RATING, AMOUNT_REVIEW, POSITIVE_AMOUNT, MIXED_AMOUNT, NEGATIVE_AMOUNT);
        List<Map<String, Object>> ratingAmount = jdbcTemplate.executeSelectSomeFields(COUNT_USER_GAME_RATING_AMOUNT_SQL,
                columnNames, userId);

        return ratingAmount;
    }

    @Override
    public List<Map<String, Object>> findMinMaxUserRating(long userId) throws DaoException {
        Set<String> columnNames = Set.of(MAX_GAME_NAME, MAX_RATING, MIN_GAME_NAME, MIN_RATING);
        List<Map<String, Object>> minMaxRating = jdbcTemplate.executeSelectSomeFields(FIND_USER_MIN_MAX_GAME_RATING_SQL,
                columnNames, userId);

        return minMaxRating;
    }

    @Override
    public Number findTotalGameRating(long gameId) throws DaoException {
        Number totalGameRating = jdbcTemplate.executeSelectCalculation(FIND_TOTAL_GAME_RATING_SQL, TOTAL_RATING, gameId);

        return totalGameRating;
    }



    @Override
    public long add(GameRating gameRating) throws DaoException {
        long gameRatingId = jdbcTemplate.executeInsertQuery(ADD_NEW_GAME_RATING_SQL,
                gameRating.getUserId(),
                gameRating.getGameId(),
                gameRating.getGameplayRating(),
                gameRating.getGraphicsRating(),
                gameRating.getSoundRating(),
                gameRating.getPlotRating(),
                gameRating.getReview());

        return gameRatingId;
    }

    @Override
    public boolean update(GameRating gameRating) throws DaoException {
        jdbcTemplate.executeUpdateDeleteFields(UPDATE_GAME_RATING_SQL,
                gameRating.getGameplayRating(),
                gameRating.getGraphicsRating(),
                gameRating.getSoundRating(),
                gameRating.getPlotRating(),
                gameRating.getReview(),
                gameRating.getUserId(),
                gameRating.getGameId());

        return true;
    }

    @Override
    public boolean delete(long gameRatingId) throws DaoException {
        boolean isDeleted = jdbcTemplate.executeUpdateDeleteFields(DELETE_REVIEW_SQL, gameRatingId);

        return isDeleted;
    }

    @Override
    public boolean deleteUserRating(long userId, long gameId) throws DaoException {
        boolean isDeleted = jdbcTemplate.executeUpdateDeleteFields(DELETE_RATING_SQL, userId, gameId);

        return isDeleted;
    }
}
