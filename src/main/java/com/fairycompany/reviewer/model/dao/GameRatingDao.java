package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.GameRating;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface provides methods to control data in database table related with game ratings.
 */
public interface GameRatingDao extends BaseDao<GameRating> {
    /**
     * Find game rating by user and game id.
     *
     * @param userId user id
     * @param gameId game id
     * @return optional with appropriate game rating
     * @throws DaoException if SQL exception occurred
     */
    Optional<GameRating> findGameRatingById(long userId, long gameId) throws DaoException;

    /**
     * Find total amount of users that rated specified game.
     *
     * @param gameId game id
     * @return total amount of users
     * @throws DaoException if SQL exception occurred
     */
    long findUserAmount(long gameId) throws DaoException;

    /**
     * Find part of all reviews for the game in accordance with pagination properties.
     *
     * @param gameId      game id
     * @param userId      user id
     * @param skippedRows the skipped rows of database table
     * @param rowAmount   the row amount of database table to get
     * @return list of user reviews
     * @throws DaoException if SQL exception occurred
     */
    List<Map<String, Object>> findReviewsForGame(long gameId, long userId, long skippedRows, int rowAmount) throws DaoException;

    /**
     * Find amount of different ratings of specified user.
     *
     * @param userId user id
     * @return amount ratings
     * @throws DaoException if SQL exception occurred
     */
    Map<String, Object> findRatingAmount(long userId) throws DaoException;

    /**
     * Find min and max game ratings of specified user.
     *
     * @param userId user id
     * @return list with min and max game ratings
     * @throws DaoException if SQL exception occurred
     */
    List<Map<String, Object>> findMinMaxUserRating(long userId) throws DaoException;

    /**
     * Find total rating of specified game from all users.
     *
     * @param gameId game id
     * @return total game rating
     * @throws DaoException if SQL exception occurred
     */
    Number findTotalGameRating(long gameId) throws DaoException;

    /**
     * Find total review amount of specified user for certain game.
     *
     * @param gameId game id
     * @param userId user id
     * @return amount of total review
     * @throws DaoException if SQL exception occurred
     */
    long findTotalGameRatingReview(long gameId, long userId) throws DaoException;

    /**
     * Delete user rating.
     *
     * @param userId user id
     * @param gameId game id
     * @return true if rating was deleted
     * @throws DaoException if SQL exception occurred
     */
    boolean deleteUserRating(long userId, long gameId) throws DaoException;
}
