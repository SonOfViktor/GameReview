package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Game;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * The interface provide methods to control data in database table related with games.
 */
public interface GameDao extends BaseDao<Game> {

    /**
     * Find part of all games with its rating in accordance with pagination properties.
     *
     * @param skippedRows the skipped rows of database table
     * @param rowAmount   the row amount of database table to get
     * @return list with games with ith rating
     * @throws DaoException if SQL exception occurred
     */
    List<Map<String, Object>> findAllGamesWithRating(long skippedRows, int rowAmount) throws DaoException;

    /**
     * Find part of all searched games with its rating in accordance with pagination properties.
     *
     * @param searchGame  searched game
     * @param skippedRows the skipped rows of database
     * @param rowAmount   the row amount of database to get
     * @return list with games with ith rating
     * @throws DaoException if SQL exception occurred
     */
    List<Map<String, Object>> searchGamesWithRating(String searchGame, long skippedRows, int rowAmount) throws DaoException;

    /**
     * Find total game amount.
     *
     * @return total game amount
     * @throws DaoException if SQL exception occurred
     */
    long findTotalGameAmount() throws DaoException;

    /**
     * Find total searched game amount.
     *
     * @param searchGame searched game
     * @return total searched game amount
     * @throws DaoException if SQL exception occurred
     */
    long findSearchedGameAmount(String searchGame) throws DaoException;

    /**
     * Find game name.
     *
     * @param name game name
     * @return list games with specified name or empty list
     * @throws DaoException if SQL exception occurred
     */
    List<Map<String, Object>> findGameName(String name) throws DaoException;

    /**
     * Add genres for certain game to database.
     *
     * @param gameId game id
     * @param set set with genres
     * @return true if genres added successfully
     * @throws DaoException if SQL exception occurred
     */
    boolean addGenres(long gameId, EnumSet<Game.Genre> set) throws DaoException;

    /**
     * Delete genres for certain game from database.
     *
     * @param gameId game id
     * @return true if genres was deleted
     * @throws DaoException if SQL exception occurred
     */
    boolean deleteGenres(long gameId) throws DaoException;
}
