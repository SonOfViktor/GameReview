package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.GameRating;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameRatingDao extends BaseDao<GameRating> {
    Optional<GameRating> findGameRatingByGameId(long userId, long gameId) throws DaoException;

    List<Map<String, Object>> findUserAmount(long gameId) throws DaoException;

    List<Map<String, Object>> findReviewsForGame(long gameId, long userId) throws DaoException;
}
