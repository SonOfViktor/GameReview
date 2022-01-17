package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Game;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public interface GameDao extends BaseDao<Game> {

    List<Map<String, Object>> findAllGamesWithRating(long skippedRows, int rowAmount) throws DaoException;

    List<Map<String, Object>> findSearchGamesWithRating(String searchGame, long skippedRows, int rowAmount) throws DaoException;

    long findTotalGameAmount() throws DaoException;

    long findSearchGameAmount(String searchGame) throws DaoException;

    boolean addGenres(long gameId, EnumSet<Game.Genre> set) throws DaoException;

    boolean deleteGenres(long gameId) throws DaoException;
}
