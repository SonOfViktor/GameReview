package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.GameDao;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.mapper.impl.GameResultSetHandler;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Date;
import java.util.*;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class GameDaoImpl implements GameDao {
    private static final Logger logger = LogManager.getLogger();
    private static GameDaoImpl instance = new GameDaoImpl();
//    private static final String COMMA_REGEX = ",";

    private static final String FIND_ALL_GAMES_MAIN_PAGE_SQL = """  
            SELECT name, publisher, release_date, description, image, price, platform, total_rating
            FROM games
            LEFT JOIN total_game_rating ON games.game_id = total_game_rating.game_id
            ORDER BY name
            """;

    private static final String FIND_ALL_GAMES_WITH_RATING_SQL = """
            SELECT games.*, GROUP_CONCAT(genre SEPARATOR ',') AS genre, total_rating FROM games
            LEFT JOIN total_game_rating ON games.game_id = total_game_rating.game_id
            JOIN games_genres ON games_genres.game_id = games.game_id
            JOIN genres ON games_genres.genre_id = genres.genre_id
            GROUP BY games_genres.game_id
            ORDER BY name LIMIT ?, ?
            """;

    private static final String FIND_TOTAL_GAME_AMOUNT_SQL = """
            SELECT COUNT(*) AS total_value FROM games
            """;

    private static final String FIND_ALL_SQL = """  
            SELECT game_id, name, publisher, developer, release_date, description, image, trailer_url, price, platform,
            FROM games
            ORDER BY name
            """;

    private static final String FIND_ALL_GENRE_SQL = """
            SELECT genre
            FROM games_genres
            JOIN genres ON games_genres.genre_id = genres.genre_id
            WHERE game_id = ?
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT games.*, GROUP_CONCAT(genre SEPARATOR ',') AS genre FROM games
            JOIN games_genres ON games_genres.game_id = games.game_id
            JOIN genres ON games_genres.genre_id = genres.genre_id
            GROUP BY games_genres.game_id
            HAVING game_id = ?
            """;

    private static final String FIND_BY_NAME_SQL = """
            SELECT game_id, name, publisher, developer, release_date, description, image, trailer_url, price, platform
            FROM games
            WHERE name = ?
            """;

    private static final String DELETE_GAME_SQL = """          
            DELETE FROM games WHERE game_id = ?
            """;

    private static final String ADD_NEW_GAME_SQL = """
            INSERT INTO games (name, publisher, developer, release_date, platform, description, image, trailer_url, price)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String ADD_GAME_GENRE_SQL = """
            INSERT INTO games_genres (game_id, genre_id)
            VALUE (?, ?)
            """;
    private static final String DELETE_GAME_GENRE_SQL = """
            DELETE FROM games_genres WHERE game_id = ?
            """;

    private static final String UPDATE_GAME_SQL = """
            UPDATE games
            SET name = ?, publisher = ?, developer = ?, platform = ?, release_date = ?, price = ?, trailer_url = ?, description = ?
            WHERE game_id = ?
            """;

    private JdbcTemplate<Game> jdbcTemplate;

    private GameDaoImpl() {
        jdbcTemplate = new JdbcTemplate<>(new GameResultSetHandler());
    }

    public static GameDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<Game> findAll() throws DaoException {
        List<Game> games = jdbcTemplate.executeSelectQuery(FIND_ALL_SQL);

        return games;
    }

    @Override
    public List<Map<String, Object>> findAllGamesWithRating(long skippedRows, int rowAmount) throws DaoException {
        Set<String> columnNames = Set.of(TOTAL_RATING);
        List<Map<String, Object>> games = jdbcTemplate.executeSelectForList(FIND_ALL_GAMES_WITH_RATING_SQL, columnNames, skippedRows, rowAmount);
        return games;
    }

    @Override
    public long findTotalGameAmount() throws DaoException {
        Number totalGameAmount = jdbcTemplate.executeSelectCalculation(FIND_TOTAL_GAME_AMOUNT_SQL, TOTAL_VALUE);
        logger.log(Level.DEBUG, "Total game amount is {}", totalGameAmount);

        return (totalGameAmount != null) ? totalGameAmount.longValue() : 0;
    }

    @Override
    public Optional<Game> findEntityById(long id) throws DaoException {
        Optional<Game> game = jdbcTemplate.executeSelectQueryForObject(FIND_BY_ID_SQL, id);

        return game;
    }

    @Override
    public Optional<Game> findGameByName(String name) throws DaoException {
        Optional<Game> game = jdbcTemplate.executeSelectQueryForObject(FIND_BY_NAME_SQL, name);

        return game;
    }

    @Override
    public boolean delete(long gameId) throws DaoException {
        boolean isDeleted = jdbcTemplate.executeUpdateDeleteFields(DELETE_GAME_SQL, gameId);

        return isDeleted;
    }

    @Override
    public long add(Game game) throws DaoException {
        long gameId = jdbcTemplate.executeInsertQuery(ADD_NEW_GAME_SQL,
                game.getName(),
                game.getPublisher(),
                game.getDeveloper(),
                Date.valueOf(game.getReleaseDate()),
                stringFromSet(game.getPlatform()),
                game.getDescription(),
                game.getImage(),
                game.getTrailerUrl(),
                game.getPrice());

        return gameId;
    }

    @Override
    public boolean update(Game game) throws DaoException {
        boolean isUpdated = jdbcTemplate.executeUpdateDeleteFields(UPDATE_GAME_SQL,
                game.getName(),
                game.getPublisher(),
                game.getDeveloper(),
                stringFromSet(game.getPlatform()),
                Date.valueOf(game.getReleaseDate()),
                game.getPrice(),
                game.getTrailerUrl(),
                game.getDescription(),
                game.getGameId());
        return isUpdated;
    }

    public boolean addGenres(long gameId, EnumSet<Game.Genre> set) throws DaoException {
        List<Object[]> batchArguments = new ArrayList<>();
        set.forEach(genre -> {
            Object[] arguments = new Object[] {gameId, genre.ordinal()};
            batchArguments.add(arguments);
        });

        jdbcTemplate.executeBatch(ADD_GAME_GENRE_SQL, batchArguments);

        return true;
    }

    public boolean deleteGenres(long gameId) throws DaoException {
        boolean isDeleted = jdbcTemplate.executeUpdateDeleteFields(DELETE_GAME_GENRE_SQL, gameId);

        return isDeleted;
    }

    private String stringFromSet(Set<Platform> platforms) {
        String stringSet = platforms.toString().replaceAll("[\\[\\]\\s]", "");
        return stringSet;
    }
}
