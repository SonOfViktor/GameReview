package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.GameDao;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.mapper.impl.GameResultSetHandler;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
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

    private static final String FIND_ALL_GAMES_WITH_RATING = """
            SELECT games.*, GROUP_CONCAT(genre SEPARATOR ',') AS genre, total_rating FROM game_rating.games
            LEFT JOIN total_game_rating ON games.game_id = total_game_rating.game_id
            JOIN games_genres ON games_genres.game_id = games.game_id
            JOIN genres ON games_genres.genre_id = genres.genre_id
            GROUP BY games_genres.game_id
            ORDER BY name LIMIT ?, ?
            """;

    private static final String FIND_TOTAL_GAME_AMOUNT = """
            SELECT COUNT(*) AS total_value FROM game_rating.games
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

    private static final String ADD_GAME_GENRE_SQL = """
            INSERT INTO games_genres (game_id, genre_id)
            VALUE (?, ?)
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

//        try (Connection connection = ConnectionPool.getInstance().getConnection();
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)){
//
//            games = createGame(resultSet);
//
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Error when finding all games. {}", e.getMessage());
//            throw new DaoException("Error when finding all games", e);
//        }
        return games;
    }

//    @Override
//    public Map<String, List<Object>> findAllGamesWithRating() throws DaoException {
//        Set<String> columnNames = Set.of(NAME, PUBLISHER, RELEASE_DATE, DESCRIPTION, IMAGE, PRICE, PLATFORM, TOTAL_RATING);
//        Map<String, List<Object>> games = jdbcTemplate.executeSelectQueryFromTables(FIND_ALL_GAMES_MAIN_PAGE_SQL, columnNames);
//
//        return games;
//    }

    @Override
    public List<Map<String, Object>> findAllGamesWithRating(long skippedRows, int rowAmount) throws DaoException {
        Set<String> columnNames = Set.of(TOTAL_RATING);
        List<Map<String, Object>> games = jdbcTemplate.executeSelectForList(FIND_ALL_GAMES_WITH_RATING, columnNames, skippedRows, rowAmount);
        return games;
    }

    @Override
    public long findTotalGameAmount() throws DaoException {
        long totalGameAmount = jdbcTemplate.executeSelectCalculation(FIND_TOTAL_GAME_AMOUNT);
        logger.log(Level.DEBUG, "Total game amount is {}", totalGameAmount);

        return totalGameAmount;
    }

    @Override
    public Optional<Game> findEntityById(long id) throws DaoException {
        Optional<Game> game = jdbcTemplate.executeSelectQueryForObject(FIND_BY_ID_SQL, id);
//        try (Connection connection = ConnectionPool.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
//            statement.setLong(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                List<Game> games = createGame(resultSet);
//                if (!games.isEmpty()) {
//                    game = Optional.of(games.get(0));
//                }
//                else {
//                    logger.log(Level.WARN, "Game with Id {} isn't found", id);
//                }
//            }
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Error when finding game with Id {}. {}", id, e.getMessage());
//            throw new DaoException("Error when finding game with Id " + id, e);
//        }

        return game;
    }

    @Override
    public Optional<Game> findGameByName(String name) throws DaoException {
        Optional<Game> game = jdbcTemplate.executeSelectQueryForObject(FIND_BY_NAME_SQL, name);
//        try (Connection connection = ConnectionPool.getInstance().getConnection();
//             PreparedStatement statement = connection.prepareStatement(FIND_BY_NAME_SQL)) {
//            statement.setString(1, name);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                List<Game> games = createGame(resultSet);
//                if (!games.isEmpty()) {
//                    game = Optional.of(games.get(0));
//                }
//                else {
//                    logger.log(Level.WARN, "Game with Id {} isn't found", name);
//                }
//            }
//        } catch (SQLException e) {
//            logger.log(Level.ERROR, "Error when finding game with Id {}. {}", name, e.getMessage());
//            throw new DaoException("Error when finding game with Id " + name, e);
//        }

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
        return false;
    }

//    private List<Game> createGame(ResultSet gameResultSet) throws SQLException {
//        List<Game> games = new ArrayList<>();
//        while (gameResultSet.next()) {
//            Game game = new Game.GameBuilder()
//                    .setGameId(gameResultSet.getLong(GAME_ID))
//                    .setName(gameResultSet.getString(NAME))
//                    .setPublisher(gameResultSet.getString(PUBLISHER))
//                    .setDeveloper(gameResultSet.getString(DEVELOPER))
//                    .setReleaseDate(gameResultSet.getDate(RELEASE_DATE).toLocalDate())
//                    .setDescription(gameResultSet.getString(DESCRIPTION))
//                    .setImage(gameResultSet.getString(IMAGE))
//                    .setPrice(gameResultSet.getBigDecimal(PRICE))
//                    .setTrailerUrl(gameResultSet.getString(TRAILER_URL))
//                    .setGenres(getGenre(gameResultSet.getLong(GAME_ID)))
//                    .setPlatforms(makePlatformSet(gameResultSet))
//                    .createGame();
//            games.add(game);
//        }
//
//        return games;
//    }

//    private EnumSet<Platform> makePlatformSet(ResultSet resultSet) throws SQLException {
//        String[] strings = resultSet.getString(PLATFORM).split(COMMA_REGEX);
//        EnumSet<Platform> platforms = EnumSet.copyOf(Arrays.stream(strings)
//                .map(o -> Platform.valueOf(o.toUpperCase()))
//                .collect(Collectors.toSet()));
//
//        return platforms;
//    }

    public EnumSet<Game.Genre> getGenre(long gameId) throws SQLException {
        EnumSet<Game.Genre> genres = null;
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

//    private long getGameIdFromDB(Game game, PreparedStatement statement) throws SQLException {
//        statement.setString(1, game.getName());
//        ResultSet resultSet = statement.executeQuery();
//        long gameId = 0;
//        if(resultSet.next()) {
//            gameId = resultSet.getLong("game_id");
//        }
//        return gameId;
//    }


    public boolean addGenres(long gameId, EnumSet<Game.Genre> set) throws DaoException {
        List<Object[]> batchArguments = new ArrayList<>();
        set.forEach(item -> {
            Object[] arguments = new Object[] {gameId, item.ordinal()};
            batchArguments.add(arguments);
        });

        jdbcTemplate.insertBatch(ADD_GAME_GENRE_SQL, batchArguments);

        return true;

//        for (Game.Genre genre : set) {
//            statement.setLong(1, gameId);
//            statement.setInt(2, genre.ordinal());
//            statement.addBatch();
//        }
//        statement.executeBatch();
    }

    private String stringFromSet(Set<Platform> platforms) {
        String stringSet = platforms.toString().replaceAll("[\\[\\]\\s]", "");
        return stringSet;
    }
}
