package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.ColumnName;
import com.fairycompany.reviewer.model.dao.GameRatingDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.impl.GameRatingDaoImpl;
import com.fairycompany.reviewer.model.entity.GameRating;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.validator.GameReviewValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class GameRatingServiceImpl implements GameRatingService {
    private static final Logger logger = LogManager.getLogger();
    private static final int ZERO = 0;
    private static final String EMPTY_LINE = "";
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private GameRatingDao gameRatingDao = GameRatingDaoImpl.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();
    private static GameRatingServiceImpl instance = new GameRatingServiceImpl();

    private GameRatingServiceImpl() {
    }

    public static GameRatingServiceImpl getInstance(){
        return instance;
    }

    public GameRating findGameRating(SessionRequestContent content) throws ServiceException {           //todo ask question
        GameRating rating = null;
        long userId = 0;
        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);

        try {
            transactionManager.initTransaction();

            if (user != null) {
                userId = user.getUserId();

                Optional<GameRating> optionalRating = gameRatingDao.findGameRatingByGameId(userId, gameId);

                if (optionalRating.isPresent()) {
                    rating = optionalRating.get();

                    int userRating = calculateUserRating(rating);
                    content.addRequestAttribute(RequestAttribute.USER_TOTAL_RATING, userRating);
                } else {
                    rating = makeDefaultGameRating(userId, gameId);
                }
            }

            Map<String, Object> userAmountMap = gameRatingDao.findUserAmount(gameId).get(0);
            long userAmount = (!userAmountMap.containsValue(null)) ? (Long) userAmountMap.get(USER_AMOUNT) : 0;
            content.addRequestAttribute(RequestAttribute.USER_AMOUNT, userAmount);

            List<Map<String, Object>> reviewsForGame = gameRatingDao.findReviewsForGame(gameId, userId);
            content.addRequestAttribute(RequestAttribute.REVIEWS_FOR_GAME, reviewsForGame);
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when finding rating of the game with id {}. {}", gameId, e.getMessage());
            throw new ServiceException("Error when finding rating of the game with id " + gameId, e);
        } finally {
            transactionManager.endTransaction();
        }

        return rating;
    }

    public boolean addUpdateGameReview(SessionRequestContent content) throws ServiceException {
        boolean isGameReviewAdded = false;
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);

        long userId = user.getUserId();
        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));
        String gameplayRating = content.getRequestParameter(RequestParameter.GAMEPLAY_RATING);
        String graphicsRating = content.getRequestParameter(RequestParameter.GRAPHICS_RATING);
        String soundRating = content.getRequestParameter(RequestParameter.SOUND_RATING);
        String plotRating = content.getRequestParameter(RequestParameter.PLOT_RATING);
        String review = (String) content.getRequestParameter(RequestParameter.REVIEW);

        review = (review == null || review.isBlank()) ? "" : review;
        List<String> allRatings = List.of(plotRating, graphicsRating, soundRating, gameplayRating);

        GameReviewValidator validator = GameReviewValidator.getInstance();

        try {
            transactionManager.initTransaction();

            if (validator.isGameRatingValid(allRatings) && validator.isReviewValid(review)) {
                GameRating rating = new GameRating.GameRatingBuilder()
                        .setUserId(userId)
                        .setGameId(gameId)
                        .setGameplayRating(Integer.parseInt(gameplayRating))
                        .setGraphicsRating(Integer.parseInt(graphicsRating))
                        .setSoundRating(Integer.parseInt(soundRating))
                        .setPlotRating(Integer.parseInt(plotRating))
                        .setReview(review)
                        .createGameRating();

                switch(content.getRequestParameter(RequestParameter.SWITCH)) {
                    case CREATE -> gameRatingDao.add(rating);
                    case UPDATE -> gameRatingDao.update(rating);
                    default -> throw new ServiceException("Parameter switch is wrong");
                }

                logger.log(Level.DEBUG, "GameRating with was added or updated");

                isGameReviewAdded = true;
            }
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when adding rating of the game with id {}. {}", gameId, e.getMessage());
            throw new ServiceException("Error when adding rating of the game with id " + gameId, e);
        } finally {
            transactionManager.endTransaction();
        }

        return isGameReviewAdded;
    }

    private GameRating makeDefaultGameRating(long userId, long gameId) {
        GameRating defaultRating = new GameRating.GameRatingBuilder()
                .setUserId(userId)
                .setGameId(gameId)
                .setGameplayRating(ZERO)
                .setGraphicsRating(ZERO)
                .setSoundRating(ZERO)
                .setPlotRating(ZERO)
                .setReview(EMPTY_LINE)
                .createGameRating();
        return defaultRating;
    }

    private int calculateUserRating(GameRating rating) {
        IntStream streamUserRating = IntStream.of(rating.getGameplayRating(), rating.getGraphicsRating(),
                rating.getSoundRating(), rating.getPlotRating());
        return (int) Math.round(streamUserRating.average().getAsDouble());
    }
}
