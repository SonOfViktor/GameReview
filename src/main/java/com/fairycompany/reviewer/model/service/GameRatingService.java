package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.GameRating;

import java.util.Map;
import java.util.Optional;

/**
 * The interface provide methods to control business logic related with game ratings.
 */
public interface GameRatingService {
    /**
     * Find game rating of certain game and user.
     *
     * @param content request content
     * @return optional with game rating or empty optional if game rating is absent
     * @throws ServiceException if DaoException occurred
     */
    Optional<GameRating> findGameRating(SessionRequestContent content) throws ServiceException;

    /**
     * Add or update user's game review.
     *
     * @param content request content
     * @return true if game review was added or updated
     * @throws ServiceException if DaoException occurred
     */
    boolean addUpdateGameReview(SessionRequestContent content) throws ServiceException;

    /**
     * Find user's amount of different ratings.
     *
     * @param content request content
     * @return user's amount of ratings
     * @throws ServiceException if DaoException occurred
     */
    Map<String, Object> findUserRatingAmount(SessionRequestContent content) throws ServiceException;

    /**
     * Delete user's game rating.
     *
     * @param content request content
     * @return true if rating wos deleted
     * @throws ServiceException if DaoException occurred
     */
    boolean deleteUserRating(SessionRequestContent content) throws ServiceException;

    /**
     * Delete user's review.
     *
     * @param content request content
     * @return true if review was deleted
     * @throws ServiceException if DaoException occurred
     */
    boolean deleteUserReview(SessionRequestContent content) throws ServiceException;

}
