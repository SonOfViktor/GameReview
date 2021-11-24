package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.ColumnName;
import com.fairycompany.reviewer.model.dao.GameRatingDao;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.mapper.impl.GameRatingResultSetHandler;
import com.fairycompany.reviewer.model.entity.GameRating;
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

    private static final String FIND_ALL_SQL = """  
            SELECT game_rating_id, user_id, game_id, gameplay_rating, graphics_rating, sound_rating, plot_rating, review
            FROM game_rating
            ORDER BY game_rating_id
            """;

    private static final String FIND_BY_GAME_ID = """
            SELECT game_rating_id, user_id, game_id, gameplay_rating, graphics_rating, sound_rating, plot_rating, review
            FROM game_rating
            WHERE user_id = ? AND game_id = ?
            """;

    private static final String FIND_USER_AMOUNT = """
            SELECT COUNT(user_id) AS user_amount
            FROM game_rating
            GROUP BY game_id
            HAVING game_id = ?
            """;

    private static final String FIND_ALL_REVIEW_FOR_GAME = """
            SELECT review, CONCAT(first_name, ' ', second_name) AS full_name
            FROM game_rating.game_rating
            JOIN users ON users.user_id = game_rating.user_id
            WHERE game_id = ? AND users.user_id <> ? AND TRIM(review) <> ''
            """;

    private static final String ADD_NEW_GAME_RATING_SQL = """
            INSERT INTO game_rating (user_id, game_id, gameplay_rating, graphics_rating, sound_rating, plot_rating, review)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_GAME_RATING_SQL = """
            UPDATE game_rating
            SET gameplay_rating = ?, graphics_rating = ?, sound_rating = ?, plot_rating = ?, review = ?
            WHERE user_id = ? AND game_id = ?
            """;

    private JdbcTemplate<GameRating> jdbcTemplate;

    private GameRatingDaoImpl() {
        jdbcTemplate = new JdbcTemplate<GameRating>(new GameRatingResultSetHandler());
    }

    public static GameRatingDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<GameRating> findAll() throws DaoException {
        List<GameRating> ratings = jdbcTemplate.executeSelectQuery(FIND_ALL_SQL);
        return ratings;
    }

    @Override
    public Optional<GameRating> findEntityById(long id) throws DaoException {           // todo
        return Optional.empty();
    }

    @Override
    public Optional<GameRating> findGameRatingByGameId(long userId, long gameId) throws DaoException {
        Optional<GameRating> rating = jdbcTemplate.executeSelectQueryForObject(FIND_BY_GAME_ID, userId, gameId);

        return rating;
    }

    @Override
    public List<Map<String, Object>> findUserAmount(long gameId) throws DaoException {
        Set<String> columnNames = Set.of(USER_AMOUNT);
        List<Map<String, Object>> userAmount = jdbcTemplate
                .executeSelectSomeFields(FIND_USER_AMOUNT, columnNames, gameId);

        return userAmount;
    }

    @Override
    public List<Map<String, Object>> findReviewsForGame(long gameId, long userId) throws DaoException {
        Set<String> columnNames = Set.of(FULL_NAME, REVIEW);
        List<Map<String, Object>> reviewsForGame = jdbcTemplate
                .executeSelectSomeFields(FIND_ALL_REVIEW_FOR_GAME, columnNames, gameId, userId);

        return reviewsForGame;
    }

    @Override
    public boolean delete(long id) throws DaoException {        //todo
        return false;
    }

    @Override
    public long add(GameRating gameRating) throws DaoException {        //todo
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

}
