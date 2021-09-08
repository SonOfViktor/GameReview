package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.GameDao;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class GameDaoImpl implements GameDao {
    private static final Logger logger = LogManager.getLogger();
    private static GameDaoImpl instance = new GameDaoImpl();
    private static final String FIND_ALL_SQL = """
            SELECT game_id, name, publisher, developer, release_date, description, image, trailer_url, price
            FROM games
            ORDER BY game_id
            """;
    private static final String FIND_ALL_PLATFORM_SQL = """
            SELECT platform
            FROM games_platforms
            JOIN platforms ON games_platforms.platform_id = platforms.platform_id
            WHERE game_id = ?
            """;
    private static final String FIND_ALL_GENRE_SQL = """
            SELECT genre
            FROM games_genres
            JOIN genres ON games_genres.genre_id = genres.genre_id
            WHERE game_id = ?
            """;

    @Override
    public List<Game> findAll() throws DaoException {
        List<Game> games;

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)){

            games = createGame(resultSet);

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding all users. {}", e.getMessage());
            throw new DaoException("Error when finding all users", e);
        }
        return games;
    }

    @Override
    public Optional<Game> findEntityById(long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public boolean delete(long id) throws DaoException {
        return false;
    }

    @Override
    public boolean add(Game game) throws DaoException {
        return false;
    }

    @Override
    public boolean update(Game game) throws DaoException {
        return false;
    }

    private List<Game> createGame(ResultSet gameResultSet) throws SQLException {
        List<Game> games = new ArrayList<>();
        while (gameResultSet.next()) {
            Game game = new Game.GameBuilder()
                    .setGameId(gameResultSet.getLong(GAME_ID))
                    .setName(gameResultSet.getString(NAME))
                    .setPublisher(gameResultSet.getString(PUBLISHER))
                    .setDeveloper(gameResultSet.getString(DEVELOPER))
                    .setReleaseDate(gameResultSet.getDate(RELEASE_DATE).toLocalDate())
                    .setDescription(gameResultSet.getString(DESCRIPTION))
                    .setImage(gameResultSet.getBlob(IMAGE))
                    .setPrice(gameResultSet.getBigDecimal(PRICE))
                    .setTrailerUrl(gameResultSet.getString(TRAILER_URL))
                    .setPlatforms(getPlatform(gameResultSet.getLong(GAME_ID)))
                    .setGenres(getGenre(gameResultSet.getLong(GAME_ID)))
                    .createGame();
            games.add(game);
        }

        return games;
    }

    private Set<Platform> getPlatform(long gameId) throws SQLException {
        Set<Platform> platforms = new HashSet<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PLATFORM_SQL)) {
            statement.setLong(1, gameId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    platforms.add(Platform.valueOf(resultSet.getString(PLATFORM).toUpperCase()));
                }
            }
        }
        return platforms;
    }

    public Set<Game.Genre> getGenre(long gameId) throws SQLException {
        Set<Game.Genre> genres = new HashSet<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_GENRE_SQL)) {
            statement.setLong(1, gameId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    genres.add(Game.Genre.valueOf(resultSet.getString(GENRE).toUpperCase()));
                }
            }
        }
        return genres;
    }
}
