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
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class GameDaoImpl implements GameDao {
    private static final Logger logger = LogManager.getLogger();
    private static GameDaoImpl instance = new GameDaoImpl();
    private static final String COMMA_REGEX = ",";

    private static final String FIND_ALL_SQL = """  
            SELECT game_id, name, publisher, developer, release_date, description, image, trailer_url, price, platform
            FROM games
            ORDER BY game_id
            """;

    private static final String FIND_ALL_GENRE_SQL = """
            SELECT genre
            FROM games_genres
            JOIN genres ON games_genres.genre_id = genres.genre_id
            WHERE game_id = ?
            """;

    private static final String FIND_BY_ID_SQL = """
            SELECT game_id, name, publisher, developer, release_date, description, image, trailer_url, price, platform
            FROM games
            WHERE game_id = ?
            """;

    private static final String FIND_BY_NAME_SQL = """
            SELECT game_id, name, publisher, developer, release_date, description, image, trailer_url, price, platform
            FROM games
            WHERE name = ?
            """;

    private static final String DELETE_BY_ID_SQL = """          
            DELETE FROM games WHERE game_id = ?
            """;

    private static final String ADD_NEW_GAME_SQL = """
            INSERT INTO games (name, publisher, developer, release_date, platform, description, image, trailer_url, price)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String GET_GAME_ID_SQL = "SELECT game_id FROM games WHERE name = ?";

    private static final String ADD_GENRE_TO_GAME_SQL = """
            INSERT INTO games_genres (game_id, genre_id)
            VALUE (?, ?)
            """;
    private GameDaoImpl() {
    }

    public static GameDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<Game> findAll() throws DaoException {
        List<Game> games;

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)){

            games = createGame(resultSet);

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding all games. {}", e.getMessage());
            throw new DaoException("Error when finding all games", e);
        }
        return games;
    }

    @Override
    public Optional<Game> findEntityById(long id) throws DaoException {
        Optional<Game> game = Optional.empty();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Game> games = createGame(resultSet);
                if (!games.isEmpty()) {
                    game = Optional.of(games.get(0));
                }
                else {
                    logger.log(Level.WARN, "Game with Id {} isn't found", id);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding game with Id {}. {}", id, e.getMessage());
            throw new DaoException("Error when finding game with Id " + id, e);
        }

        return game;
    }

    @Override
    public Optional<Game> findGameByName(String name) throws DaoException {
        Optional<Game> game = Optional.empty();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Game> games = createGame(resultSet);
                if (!games.isEmpty()) {
                    game = Optional.of(games.get(0));
                }
                else {
                    logger.log(Level.WARN, "Game with Id {} isn't found", name);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding game with Id {}. {}", name, e.getMessage());
            throw new DaoException("Error when finding game with Id " + name, e);
        }

        return game;
    }

    @Override
    public boolean delete(long id) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement gameStatement = connection.prepareStatement(DELETE_BY_ID_SQL)) {

            gameStatement.setLong(1, id);
            gameStatement.executeUpdate();

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when deleting game with Id {}. {}", id, e.getMessage());
            throw new DaoException("Error when deleting game with Id " + id, e);
        }

        return true;
    }

    @Override
    public boolean add(Game game) throws DaoException {
        Connection connection = ConnectionPool.getInstance().getConnection();

        try (PreparedStatement statement = connection.prepareStatement(ADD_NEW_GAME_SQL);
             PreparedStatement idStatement = connection.prepareStatement(GET_GAME_ID_SQL);
             PreparedStatement genreStatement = connection.prepareStatement(ADD_GENRE_TO_GAME_SQL)) {

            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            statement.setString(1, game.getName());
            statement.setString(2, game.getPublisher());
            statement.setString(3, game.getDeveloper());
            statement.setDate(4, Date.valueOf(game.getReleaseDate()
            ));
            statement.setString(5, stringFromSet(game.getPlatform()));
            statement.setString(6, game.getDescription());
            statement.setBlob(7, game.getImage());
            statement.setString(8, game.getTrailerUrl());
            statement.setBigDecimal(9, game.getPrice());
            statement.executeUpdate();

            long gameId = getGameIdFromDB(game, idStatement);
            game.setGameId(gameId);

            setGenresToDB(game, genreStatement);

            connection.commit();
        } catch (SQLException e) {
            rollback(connection);
            logger.log(Level.ERROR, "Error when adding game. {}", e.getMessage());
            throw new DaoException("Error when adding game", e);
        } finally {
            close(connection);
        }
        return true;
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
                    .setGenres(getGenre(gameResultSet.getLong(GAME_ID)))
                    .setPlatforms(makePlatformSet(gameResultSet))
                    .createGame();
            games.add(game);
        }

        return games;
    }

    private Set<Platform> makePlatformSet(ResultSet resultSet) throws SQLException {
        String[] strings = resultSet.getString(PLATFORM).split(COMMA_REGEX);
        Set<Platform> platforms = Arrays.stream(strings)
                .map(o -> Platform.valueOf(o.toUpperCase()))
                .collect(Collectors.toSet());

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

    // todo replace to BaseDao
    private void rollback(Connection connection) {
        try {
            connection.rollback();
            logger.log(Level.DEBUG, "Rollback executed");
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Rollback didn't execute. {}", e.getMessage());
            // todo DaoException?
        }
    }

    private long getGameIdFromDB(Game game, PreparedStatement statement) throws SQLException {
        statement.setString(1, game.getName());
        ResultSet resultSet = statement.executeQuery();
        long gameId = 0;
        if(resultSet.next()) {
            gameId = resultSet.getLong("game_id");
        }
        return gameId;
    }

    private void setGenresToDB(Game game, PreparedStatement statement) throws SQLException {
        for (Game.Genre genre : game.getGenres()) {                 // todo make private method
            statement.setLong(1, game.getGameId());
            statement.setInt(2, genre.ordinal());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    private String stringFromSet(Set<Platform> platforms) {
        String stringSet = platforms.toString().replaceAll("[\\[\\]\\s]", "").toLowerCase();
        return stringSet;
    }
}
