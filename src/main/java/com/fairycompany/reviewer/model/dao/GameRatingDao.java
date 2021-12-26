package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.GameRating;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameRatingDao extends BaseDao<GameRating> {
    Optional<GameRating> findGameRatingByGameId(long userId, long gameId) throws DaoException;

    long findUserAmount(long gameId) throws DaoException;

    List<Map<String, Object>> findReviewsForGame(long gameId, long userId, long skippedRows, int rowAmount) throws DaoException;

    List<Map<String, Object>> findRatingAmount(long userId) throws DaoException;

    List<Map<String, Object>> findMinMaxUserRating(long userId) throws DaoException;

    Number findTotalGameRating(long gameId) throws DaoException;

    long findTotalGameRatingReview(long gameId, long userId) throws DaoException;

    boolean deleteUserRating(long userId, long gameId) throws DaoException;

    boolean deleteUserReview(long gameRatingId) throws DaoException;
}
