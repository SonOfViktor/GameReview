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
import com.fairycompany.reviewer.model.util.ServiceUtil;
import com.fairycompany.reviewer.model.validator.CommonValidator;
import com.fairycompany.reviewer.model.validator.GameValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.fairycompany.reviewer.controller.command.RequestParameter.*;

public class GameServiceImpl implements GameService {
    private static final Logger logger = LogManager.getLogger();
    private static final String ANY_SYMBOLS_SQL_PATTERN = "%";
    private static final String RELATIVE_IMAGE_DIR = "media\\game\\";
    private static final String DEFAULT_FILE = "pic\\default_game.jpg";
    private static final String WATCH_PART_REF = "watch?v=";
    private static final String EMBED_PART_REF = "embed/";
    private static final String AUTOPLAY_PART_REF = "?&autoplay=1";
    private static final BigDecimal INVALID_PRICE = new BigDecimal(-1);
    private static GameServiceImpl instance = new GameServiceImpl();
    private GameDao gameDao = GameDaoImpl.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();
    ServiceUtil serviceUtil = ServiceUtil.getInstance();

    private GameServiceImpl() {
    }

    public static GameServiceImpl getInstance(){
        return instance;
    }

    public List<Map<String, Object>> findAllGamesWithRating(SessionRequestContent content) throws ServiceException {
        long actualPage = serviceUtil.takeActualPage(content);
        int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());

        List<Map<String, Object>> games;
        try {
            transactionManager.initTransaction();

            long skippedGames = actualPage * rowAmount - rowAmount;
            games = gameDao.findAllGamesWithRating(skippedGames, rowAmount);

            long totalGameAmount = gameDao.findTotalGameAmount();
            long pageAmount = (long) Math.ceil((double) totalGameAmount / rowAmount);

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

    public List<Map<String, Object>> findSearchedGamesWithRating(SessionRequestContent content) throws ServiceException {
        List<Map<String, Object>> games;
        GameValidator validator = GameValidator.getInstance();
        ServiceUtil serviceUtil = ServiceUtil.getInstance();
        String searchField = content.getRequestParameter(SEARCH_FIELD);

        if(validator.isSearchFieldValid(searchField)) {
            searchField = searchField + ANY_SYMBOLS_SQL_PATTERN;
        } else {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.SEARCH_ERROR);
            searchField = ANY_SYMBOLS_SQL_PATTERN;
        }

        long actualPage = serviceUtil.takeActualPage(content);
        int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());

        try {
            transactionManager.initTransaction();

            long skippedGames = actualPage * rowAmount - rowAmount;
            games = gameDao.searchGamesWithRating(searchField, skippedGames, rowAmount);

            long totalGameAmount = gameDao.findSearchedGameAmount(searchField);
            long pageAmount = (long) Math.ceil((double) totalGameAmount / rowAmount);

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

    public Optional<Game> findGame(SessionRequestContent content) throws ServiceException {
        Optional<Game> game = Optional.empty();
        String gameIdString = content.getRequestParameter(RequestParameter.GAME_ID);

        CommonValidator commonValidator = CommonValidator.getInstance();
        if (commonValidator.isStringLong(gameIdString)) {
            long gameId = Long.parseLong(gameIdString);
            try {
                transactionManager.initTransaction();

                game = gameDao.findEntityById(gameId);
            } catch (DaoException e) {
                logger.log(Level.ERROR, "Error when finding the game with id {}, {}", gameId, e.getMessage());
                throw new ServiceException("Error when finding the game with id " + gameId, e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return game;
    }

    public boolean addGame(SessionRequestContent content) throws ServiceException {
        Game.GameBuilder gameBuilder = new Game.GameBuilder();
        boolean isGameAdded = false;
        String[] genres = content.getRequestParameterValues(GENRE);

        GameValidator gameValidator = GameValidator.getInstance();

        if (makeGameBuilder(content, gameBuilder) && gameValidator.isGenresValid(genres)) {
            Game game = gameBuilder.createGame();

            if(isGameExist(game.getName())){
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.GAME_ALREADY_EXIST);
                return isGameAdded;
            }

            String uploadDirectory = (String) content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY);
            String fileExtension = (String) content.getRequestAttribute(RequestAttribute.FILE_EXTENSION);
            Optional<InputStream> imageStream = (Optional<InputStream>) content.getRequestAttribute(RequestAttribute.IMAGE_INPUT_STREAM);

            String imagePath = RELATIVE_IMAGE_DIR + game.getName();
            serviceUtil.makeDir(uploadDirectory + RELATIVE_IMAGE_DIR);

            try {
                imagePath = (imageStream.isPresent()) ?
                        serviceUtil.saveImage(uploadDirectory, imagePath, fileExtension, imageStream.get()) :
                        serviceUtil.saveDefaultImage(uploadDirectory, imagePath, DEFAULT_FILE);
                game.setImage(imagePath);

                transactionManager.initTransaction();

                long gameId = gameDao.add(game);

                gameDao.addGenres(gameId, makeEnumSet(genres, Game.Genre.class));

                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.ADD_GAME_SUCCESSFUL);
                isGameAdded = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                serviceUtil.deleteFile(uploadDirectory + imagePath);
                logger.log(Level.ERROR, "Error when adding game {}, {}", game.getName(), e.getMessage());
                throw new ServiceException("Error when adding game " + game.getName(), e);
            } finally {
                transactionManager.endTransaction();
            }
        } else {
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ADD_GAME_FAILED);
        }

        return isGameAdded;
    }

    @Override
    public void addGameToShoppingCart(SessionRequestContent content) throws ServiceException {
        String platformString = content.getRequestParameter(PLATFORM);
        Optional<Game> optionalGame = findGame(content);

        GameValidator validator = GameValidator.getInstance();
        if (validator.isPlatformValid(platformString) && optionalGame.isPresent()) {
            Platform platform = Platform.valueOf(platformString);
            Game game = optionalGame.get();

            Order order = new Order.OrderBuilder().setGameName(game.getName()).setPlatform(platform).createOrder();
            Map<Order, Game> shoppingCart = (Map) content.getSessionAttribute(SessionAttribute.SHOPPING_CART);

            Game unusedGame = (!shoppingCart.containsKey(order)) ? shoppingCart.put(order, game) : shoppingCart.remove(order);
        }
    }

    @Override
    public void deleteGameFromShoppingCart(SessionRequestContent content) {
        String gameName = content.getRequestParameter(GAME_NAME);
        String platformString = content.getRequestParameter(PLATFORM);

        GameValidator validator = GameValidator.getInstance();

        if (validator.isPlatformValid(platformString) && validator.isDataFieldValid(gameName)) {
            Platform platform = Platform.valueOf(platformString);

            Order order = new Order.OrderBuilder().setGameName(gameName).setPlatform(platform).createOrder();
            Map<Order, Game> shoppingCart = (Map) content.getSessionAttribute(SessionAttribute.SHOPPING_CART);

            shoppingCart.remove(order);
        }
    }

    @Override
    public boolean updateGame(SessionRequestContent content) throws ServiceException{
        Game.GameBuilder gameBuilder = new Game.GameBuilder();
        boolean isGameUpdated = false;

        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));
        if (makeGameBuilder(content, gameBuilder)) {
            Game game = gameBuilder.createGame();
            game.setGameId(gameId);

            try {
                transactionManager.initTransaction();

                gameDao.update(game);

                transactionManager.commit();

                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.UPDATE_GAME_SUCCESS);
                isGameUpdated = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when updating game with name {}, {}", game.getName(), e.getMessage());
                throw new ServiceException("Error when updating game with name " + game.getName(), e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isGameUpdated;
    }

    public boolean updateGenres(SessionRequestContent content) throws ServiceException {
        boolean isGenresUpdated = false;

        String gameIdString = content.getRequestParameter(RequestParameter.GAME_ID);
        String[] genres = content.getRequestParameterValues(GENRE);

        CommonValidator commonValidator = CommonValidator.getInstance();
        GameValidator gameValidator = GameValidator.getInstance();

        if (gameValidator.isGenresValid(genres) && commonValidator.isStringLong(gameIdString)) {
            long gameId = Long.parseLong(gameIdString);

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
        boolean isDeleted = false;
        GameValidator gameValidator = GameValidator.getInstance();
        CommonValidator commonValidator = CommonValidator.getInstance();

        String gameIdString = content.getRequestParameter(RequestParameter.GAME_ID);
        String uploadDirectory = (String) content.getRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY);
        String gameImage = content.getRequestParameter(IMAGE);

        if (commonValidator.isStringLong(gameIdString) && gameValidator.isImageAddressValid(gameImage)) {
            long gameId = Long.parseLong(gameIdString);

            try {
                transactionManager.initTransaction();

                gameDao.delete(gameId);

                serviceUtil.deleteFile(uploadDirectory + gameImage);

                transactionManager.commit();

                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.DELETE_GAME_SUCCESS);
                isDeleted = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when updating genres. {}", e.getMessage());
                throw new ServiceException("Error when updating genres", e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isDeleted;
    }

    private static <T extends Enum<T>> EnumSet<T> makeEnumSet(String[] array, Class<T> enumClass) {
        Set<T> set = Arrays.stream(array).map(s -> Enum.valueOf(enumClass, s.toUpperCase())).collect(Collectors.toSet());
        return EnumSet.copyOf(set);
    }

    private boolean makeGameBuilder(SessionRequestContent content, Game.GameBuilder gameBuilder) {
        boolean isGameMade = false;
        String name = content.getRequestParameter(GAME_NAME);
        String publisher = content.getRequestParameter(PUBLISHER);
        String developer = content.getRequestParameter(DEVELOPER);
        String[] platforms = content.getRequestParameterValues(PLATFORM);
        LocalDate releaseDate = serviceUtil.getDateFromString(content.getRequestParameter(RELEASE_DATE));
        BigDecimal price = takePriceFromContent(content);
        String youtubeUrlRaw = content.getRequestParameter(YOUTUBE_URL);
        String description = content.getRequestParameter(GAME_DESCRIPTION);

        GameValidator validator = GameValidator.getInstance();

        if (validator.isAllGameDataValid(name, publisher, developer, platforms,
                releaseDate, price, youtubeUrlRaw, description)) {
            String youtubeUrl = youtubeUrlRaw.replace(WATCH_PART_REF, EMBED_PART_REF).concat(AUTOPLAY_PART_REF);

            gameBuilder.setName(name)
                    .setPublisher(publisher)
                    .setDeveloper(developer)
                    .setPlatforms(makeEnumSet(platforms, Platform.class))
                    .setReleaseDate(releaseDate)
                    .setPrice(price)
                    .setTrailerUrl(youtubeUrl)
                    .setDescription(description);

            isGameMade = true;
        }

        return isGameMade;
    }

    private BigDecimal takePriceFromContent(SessionRequestContent content) {
        GameValidator validator = GameValidator.getInstance();
        String priceString = content.getRequestParameter(PRICE);
        BigDecimal price = (validator.isPriceBigDecimal(priceString)) ? new BigDecimal(priceString) : INVALID_PRICE;

        return price;
    }

    private boolean isGameExist(String name) throws ServiceException {
        List<Map<String, Object>> gameName;

        try {
            transactionManager.initTransaction();

            gameName = gameDao.findGameName(name);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when checking game existence, {}", e.getMessage());
            throw new ServiceException("Error when checking game existence", e);
        }

        return !gameName.isEmpty();
    }
}
