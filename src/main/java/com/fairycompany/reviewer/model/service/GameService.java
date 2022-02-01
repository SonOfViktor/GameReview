package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The interface provide methods to control business logic related with game.
 */
public interface GameService {

    /**
     * Find all games with its total rating.
     *
     * @param content the content
     * @return the list with games and its total ratings
     * @throws ServiceException DaoException occurred
     */
    List<Map<String, Object>> findAllGamesWithRating(SessionRequestContent content) throws ServiceException;

    /**
     * Find searched games with rating.
     *
     * @param content request content
     * @return the list with searched games and ith total ratings
     * @throws ServiceException DaoException occurred
     */
    List<Map<String, Object>> findSearchedGamesWithRating(SessionRequestContent content) throws ServiceException;

    /**
     * Find certain game.
     *
     * @param content request content
     * @return the optional with certain game or empty optional if the game wasn't found
     * @throws ServiceException DaoException occurred
     */
    Optional<Game> findGame(SessionRequestContent content) throws ServiceException;

    /**
     * Add game to data-base.
     *
     * @param content the request content
     * @return true if game was added
     * @throws ServiceException DaoException occurred
     */
    boolean addGame(SessionRequestContent content) throws ServiceException;

    /**
     * Add game to shopping cart.
     *
     * @param content request content
     * @throws ServiceException DaoException occurred
     */
    void addGameToShoppingCart(SessionRequestContent content) throws ServiceException;

    /**
     * Delete game from shopping cart.
     *
     * @param content request content
     */
    void deleteGameFromShoppingCart(SessionRequestContent content);

    /**
     * Update game data.
     *
     * @param content request content
     * @return true if game data was updated
     * @throws ServiceException DaoException occurred
     */
    boolean updateGame(SessionRequestContent content) throws ServiceException;

    /**
     * Update game's genres.
     *
     * @param content request content
     * @return true if genres was updated
     * @throws ServiceException DaoException occurred
     */
    boolean updateGenres(SessionRequestContent content) throws ServiceException;

    /**
     * Delete game from data-base.
     *
     * @param content request content
     * @return true if game was deleted
     * @throws ServiceException DaoException occurred
     */
    boolean deleteGame(SessionRequestContent content) throws  ServiceException;
}
