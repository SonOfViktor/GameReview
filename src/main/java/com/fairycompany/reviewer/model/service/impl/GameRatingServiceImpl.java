package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.GameRatingDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.impl.GameRatingDaoImpl;
import com.fairycompany.reviewer.model.entity.GameRating;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.util.ServiceUtil;
import com.fairycompany.reviewer.model.validator.CommonValidator;
import com.fairycompany.reviewer.model.validator.GameReviewValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static com.fairycompany.reviewer.controller.command.RequestParameter.ACTUAL_PAGE;
import static com.fairycompany.reviewer.controller.command.RequestParameter.ROW_AMOUNT;

public class GameRatingServiceImpl implements GameRatingService {
    private static final Logger logger = LogManager.getLogger();
    private static final String CREATE = "create";
    private static final String UPDATE = "update";
    private static final long NON_EXIST_ID = 0;
    private GameRatingDao gameRatingDao = GameRatingDaoImpl.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();
    private static GameRatingServiceImpl instance = new GameRatingServiceImpl();

    private GameRatingServiceImpl() {
    }

    public static GameRatingServiceImpl getInstance(){
        return instance;
    }

    public Optional<GameRating> findGameRating(SessionRequestContent content) throws ServiceException {
        Optional<GameRating> optionalRating = Optional.empty();
        CommonValidator commonValidator = CommonValidator.getInstance();
        String gameIdString = content.getRequestParameter(RequestParameter.GAME_ID);

        if(!commonValidator.isStringLong(gameIdString)){
            return optionalRating;
        }

        long gameId = Long.parseLong(gameIdString);
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        long userId = (user != null) ? user.getUserId() : NON_EXIST_ID;

        try {
            transactionManager.initTransaction();

            optionalRating = gameRatingDao.findGameRatingById(userId, gameId);

            if (optionalRating.isPresent()) {
                GameRating rating = optionalRating.get();
                int userRating = calculateUserRating(rating);
                content.addRequestAttribute(RequestAttribute.USER_TOTAL_RATING, userRating);
            } else {
                optionalRating = Optional.of(new GameRating());
            }

            Number totalGameRating = gameRatingDao.findTotalGameRating(gameId);
            content.addRequestAttribute(RequestAttribute.TOTAL_GAME_RATING, totalGameRating);

            long userAmount = gameRatingDao.findUserAmount(gameId);
            content.addRequestAttribute(RequestAttribute.USER_AMOUNT, userAmount);

            ServiceUtil serviceUtil = ServiceUtil.getInstance();

            long actualPage = serviceUtil.takeActualPage(content);
            int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());
            long skippedGames = actualPage * rowAmount - rowAmount;
            List<Map<String, Object>> reviewsForGame = gameRatingDao.findReviewsForGame(gameId, userId, skippedGames, rowAmount);

            long totalGameRatingReview = gameRatingDao.findTotalGameRatingReview(gameId, userId);
            long pageAmount = (long) Math.ceil((double) totalGameRatingReview / rowAmount);

            content.addRequestAttribute(RequestAttribute.PAGE_AMOUNT, pageAmount);
            content.addRequestAttribute(RequestAttribute.ACTUAL_PAGE, actualPage);
            content.addRequestAttribute(RequestAttribute.REVIEWS_FOR_GAME, reviewsForGame);
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when finding rating of the game with id {}. {}", gameId, e.getMessage());
            throw new ServiceException("Error when finding rating of the game with id " + gameId, e);
        } finally {
            transactionManager.endTransaction();
        }

        return optionalRating;
    }

    public boolean findUserRatingAmount(SessionRequestContent content) throws ServiceException {
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        long userId = user.getUserId();

        try {
            transactionManager.initTransaction();

            List<Map<String, Object>> userRatingAmount = gameRatingDao.findRatingAmount(userId);
            content.addRequestAttribute(RequestAttribute.USER_RATING_AMOUNT, userRatingAmount.get(0));

            List<Map<String, Object>> minMaxUserRating = gameRatingDao.findMinMaxUserRating(userId);
            if (!minMaxUserRating.isEmpty()) {
                content.addRequestAttribute(RequestAttribute.MIN_MAX_USER_RATING, minMaxUserRating.get(0));
            }
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when finding amount of rating of this user {}. {}", userId, e.getMessage());
            throw new ServiceException("Error when finding amount of rating of this user " + userId, e);
        } finally {
            transactionManager.endTransaction();
        }

        return true;
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
        String review = content.getRequestParameter(RequestParameter.REVIEW);

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
                    default -> throw new ServiceException("Switch parameter is wrong");
                }

                logger.log(Level.DEBUG, "GameRating was added or updated");

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

    @Override
    public boolean deleteUserRating(SessionRequestContent content) throws ServiceException {
        boolean isDeleted = false;
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        long userId = user.getUserId();
        String gameIdString = content.getRequestParameter(RequestParameter.GAME_ID);

        CommonValidator commonValidator = CommonValidator.getInstance();

        if (commonValidator.isStringLong(gameIdString)) {
            long gameId = Long.parseLong(gameIdString);

            try {
                transactionManager.initTransaction();

                gameRatingDao.deleteUserRating(userId, gameId);

                transactionManager.commit();

                isDeleted = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when deleting user's rating. {}", e.getMessage());
                throw new ServiceException("Error when deleting user's rating", e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isDeleted;
    }

    @Override
    public boolean deleteUserReview(SessionRequestContent content) throws ServiceException {
        boolean isDeleted = false;
        String gameRatingIdString =content.getRequestParameter(RequestParameter.GAME_RATING_ID);

        CommonValidator commonValidator = CommonValidator.getInstance();

        if(commonValidator.isStringLong(gameRatingIdString)) {
            long gameRatingId = Long.parseLong(gameRatingIdString);

            try {
                transactionManager.initTransaction();

                gameRatingDao.delete(gameRatingId);

                transactionManager.commit();

                isDeleted = true;
            } catch (DaoException e) {
                transactionManager.rollback();
                logger.log(Level.ERROR, "Error when deleting user's comment. {}", e.getMessage());
                throw new ServiceException("Error when deleting user's comment", e);
            } finally {
                transactionManager.endTransaction();
            }
        }

        return isDeleted;
    }

    private int calculateUserRating(GameRating rating) {
        IntStream streamUserRating = IntStream.of(rating.getGameplayRating(), rating.getGraphicsRating(),
                rating.getSoundRating(), rating.getPlotRating());
        return (int) Math.round(streamUserRating.average().getAsDouble());
    }
}
