package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.GameDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.impl.GameDaoImpl;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.util.ServiceUtil;
import com.fairycompany.reviewer.model.validator.GameValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;

public class GameServiceImpl implements GameService {
    private static final Logger logger = LogManager.getLogger();
    private static final String ANY_SYMBOLS_SQL_PATTERN = "%";
    private GameDao gameDao = GameDaoImpl.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();
    private static GameServiceImpl instance = new GameServiceImpl();

    private GameServiceImpl() {
    }

    public static GameServiceImpl getInstance(){
        return instance;
    }

    public List<Map<String, Object>> findAllGamesWithRating(SessionRequestContent content) throws ServiceException {
        int actualPage = Integer.parseInt(content.getRequestParameter(ACTUAL_PAGE));
        int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());

        logger.log(Level.DEBUG, "Actual game page is {}", actualPage);

        List<Map<String, Object>> games;
        try {
            transactionManager.initTransaction();

            long skippedGames = (long) actualPage * rowAmount - rowAmount;
            games = gameDao.findAllGamesWithRating(skippedGames, rowAmount);

            long totalGameAmount = gameDao.findTotalGameAmount();
            int pageAmount = (int) Math.ceil((double) totalGameAmount / rowAmount);

            content.addRequestAttribute(RequestAttribute.PAGE_AMOUNT, pageAmount);
            content.addRequestAttribute(RequestAttribute.ACTUAL_PAGE, actualPage);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when finding games, {}", e.getMessage());
            throw new ServiceException("Error when finding games", e);
        } finally {
            transactionManager.endTransaction();
        }

        return games;
    }

    public List<Map<String, Object>> findSearchGamesWithRating(SessionRequestContent content) throws ServiceException {
        List<Map<String, Object>> games;
        GameValidator validator = GameValidator.getInstance();
        String searchField = content.getRequestParameter(SEARCH_FIELD);

        if(validator.isSearchFieldValid(searchField)) {
            searchField = searchField + ANY_SYMBOLS_SQL_PATTERN;
        } else {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.SEARCH_ERROR);
            searchField = ANY_SYMBOLS_SQL_PATTERN;
        }

        int actualPage = Integer.parseInt(content.getRequestParameter(ACTUAL_PAGE));
        int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());

        logger.log(Level.DEBUG, "Actual game page is {}", actualPage);

        try {
            transactionManager.initTransaction();

            long skippedGames = (long) actualPage * rowAmount - rowAmount;
            games = gameDao.findSearchGamesWithRating(searchField, skippedGames, rowAmount);

            long totalGameAmount = gameDao.findSearchGameAmount(searchField);
            int pageAmount = (int) Math.ceil((double) totalGameAmount / rowAmount);

            content.addRequestAttribute(RequestAttribute.PAGE_AMOUNT, pageAmount);
            content.addRequestAttribute(RequestAttribute.ACTUAL_PAGE, actualPage);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when finding games, {}", e.getMessage());
            throw new ServiceException("Error when finding games", e);
        } finally {
            transactionManager.endTransaction();
        }
        return games;
    }

    public Optional<Game> findGame(long gameId) throws ServiceException {
        Optional<Game> game;
        try {
            transactionManager.initTransaction();

            game = gameDao.findEntityById(gameId);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when finding the game, {}", e.getMessage());
            throw new ServiceException("Error when finding the game", e);
        } finally {
            transactionManager.endTransaction();
        }

        return game;
    }

    public boolean addGame(SessionRequestContent content) throws ServiceException {
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        boolean isGameAdded = false;

        String name = content.getRequestParameter(GAME_NAME);
        String publisher = content.getRequestParameter(PUBLISHER);
        String developer = content.getRequestParameter(DEVELOPER);
        String[] platforms = content.getRequestParameterValues(PLATFORM);
        LocalDate releaseDate = serviceUtil.getDateFromString(content.getRequestParameter(RELEASE_DATE));
        BigDecimal price = new BigDecimal(content.getRequestParameter(PRICE));
        String[] genres = content.getRequestParameterValues(GENRE);
        String youtubeUrlRaw = content.getRequestParameter(YOUTUBE_URL);
        String youtubeUrl = youtubeUrlRaw.replace("watch?v=","embed/").concat("?&autoplay=1");
        String description = content.getRequestParameter(GAME_DESCRIPTION);
        String gameImage = (String) content.getRequestAttribute(RequestAttribute.GAME_IMAGE);

        GameValidator validator = GameValidator.getInstance();

        if (validator.isStringFieldValid(name) && validator.isStringFieldValid(publisher) &&
            validator.isStringFieldValid(developer) && validator.isCheckBoxDataValid(platforms) &&
            validator.isReleaseDateValid(releaseDate) && validator.isPriceValid(price) &&
            validator.isCheckBoxDataValid(genres) && validator.isYoutubeUrlValid(youtubeUrl) &&
            validator.isDescriptionValid(description)) {

            Game game = new Game.GameBuilder()
                    .setName(name)
                    .setPublisher(publisher)
                    .setDeveloper(developer)
                    .setPlatforms(makeEnumSet(platforms, Platform.class))
                    .setReleaseDate(releaseDate)
                    .setPrice(price)
                    .setTrailerUrl(youtubeUrl)
                    .setDescription(description)
                    .setImage(gameImage)
                    .createGame();

            try {
                transactionManager.initTransaction();

                long gameId = gameDao.add(game);

                gameDao.addGenres(gameId, makeEnumSet(genres, Game.Genre.class));

                isGameAdded = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when adding game {}, {}", name, e.getMessage());
                throw new ServiceException("Error when adding game " + name, e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isGameAdded;
    }

    @Override
    public void addGameToShoppingCart(SessionRequestContent content) throws ServiceException {
        long gameId = Long.parseLong(content.getRequestParameter(GAME_ID));
        Platform platform = Platform.valueOf(content.getRequestParameter(PLATFORM));
        Game game = findGame(gameId).get();
        Order order = new Order.OrderBuilder().setGameName(game.getName()).setPlatform(platform).createOrder();
        Map<Order, Game> shoppingCart = (Map) content.getSessionAttribute(SessionAttribute.SHOPPING_CART);
        if (!shoppingCart.containsKey(order)) {
            shoppingCart.put(order, game);
        } else {
            shoppingCart.remove(order);
        }
    }

    @Override
    public void deleteGameFromShoppingCart(SessionRequestContent content) {
        String gameName = content.getRequestParameter(GAME_NAME);
        Platform platform = Platform.valueOf(content.getRequestParameter(PLATFORM));
        Order order = new Order.OrderBuilder().setGameName(gameName).setPlatform(platform).createOrder();
        Map<Order, Game> shoppingCart = (Map) content.getSessionAttribute(SessionAttribute.SHOPPING_CART);

        shoppingCart.remove(order);
    }

    @Override
    public boolean updateGame(SessionRequestContent content) throws ServiceException{
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        boolean isGameUpdated = false;

        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));
        String name = content.getRequestParameter(GAME_NAME);
        String publisher = content.getRequestParameter(PUBLISHER);
        String developer = content.getRequestParameter(DEVELOPER);
        String[] platforms = content.getRequestParameterValues(PLATFORM);
        LocalDate releaseDate = serviceUtil.getDateFromString(content.getRequestParameter(RELEASE_DATE));
        BigDecimal price = new BigDecimal(content.getRequestParameter(PRICE));
        String youtubeUrlRaw = content.getRequestParameter(YOUTUBE_URL);
        String youtubeUrl = youtubeUrlRaw.replace("watch?v=","embed/").concat("?&autoplay=1");
        String description = content.getRequestParameter(GAME_DESCRIPTION);

        GameValidator validator = GameValidator.getInstance();

        if (validator.isStringFieldValid(name) && validator.isStringFieldValid(publisher) &&
                validator.isStringFieldValid(developer) && validator.isCheckBoxDataValid(platforms) &&
                validator.isReleaseDateValid(releaseDate) && validator.isPriceValid(price) &&
                validator.isYoutubeUrlValid(youtubeUrl) && validator.isDescriptionValid(description)) {
            try {
                transactionManager.initTransaction();

                Game newGame = new Game.GameBuilder()
                        .setGameId(gameId)
                        .setName(name)
                        .setPublisher(publisher)
                        .setDeveloper(developer)
                        .setPlatforms(makeEnumSet(platforms, Platform.class))
                        .setReleaseDate(releaseDate)
                        .setPrice(price)
                        .setTrailerUrl(youtubeUrl)
                        .setDescription(description)
                        .createGame();

                gameDao.update(newGame);

                transactionManager.commit();

                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.UPDATE_GAME_SUCCESS);
                isGameUpdated = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when updating game with name {}, {}", name, e.getMessage());
                throw new ServiceException("Error when updating game with name " + name, e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isGameUpdated;
    }

    public boolean updateGenres(SessionRequestContent content) throws ServiceException {
        boolean isGenresUpdated = false;

        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));
        String[] genres = content.getRequestParameterValues(GENRE);

        GameValidator validator = GameValidator.getInstance();

        if (validator.isCheckBoxDataValid(genres)) {
            try {
                transactionManager.initTransaction();

                gameDao.deleteGenres(gameId);
                gameDao.addGenres(gameId, makeEnumSet(genres, Game.Genre.class));

                transactionManager.commit();

                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.UPDATE_GENRE_SUCCESS);

                isGenresUpdated = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when updating genres. {}", e.getMessage());
                throw new ServiceException("Error when updating genres", e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isGenresUpdated;
    }

    @Override
    public boolean deleteGame(SessionRequestContent content) throws ServiceException {
        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));

        try {
            transactionManager.initTransaction();

            gameDao.delete(gameId);

            transactionManager.commit();

            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.DELETE_GAME_SUCCESS);
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when updating genres. {}", e.getMessage());
            throw new ServiceException("Error when updating genres", e);
        } finally {
            transactionManager.endTransaction();
        }
        return true;
    }

    //    public List<String> makeGenreNames() {
//        List<String> genres = Arrays.stream(Game.Genre.values())
//                .map(Enum::name)
//                .peek(String::toLowerCase)
//                .toList();
//        return genres;
//    }

    private Game takeGameFromList(SessionRequestContent content) {
        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));
        List<Map<String, Object>> gameList =
                (List<Map<String, Object>>) content.getSessionAttribute(SessionAttribute.GAME_LIST);

        Game game = gameList.stream()
                .map(gameMap -> Game.class.cast(gameMap.get(RequestAttribute.GAME)))
                .filter(g -> g.getGameId() == gameId)
                .findFirst()
                .get();

        return game;
    }

    private static <T extends Enum<T>> EnumSet<T> makeEnumSet(String[] array, Class<T> enumClass) {
        Set<T> set = Arrays.stream(array).map(s -> Enum.valueOf(enumClass, s.toUpperCase())).collect(Collectors.toSet());
        return EnumSet.copyOf(set);
    }
}
