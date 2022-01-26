package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GameService {

    List<Map<String, Object>> findAllGamesWithRating(SessionRequestContent content) throws ServiceException;

    List<Map<String, Object>> findSearchGamesWithRating(SessionRequestContent content) throws ServiceException;

    Optional<Game> findGame(SessionRequestContent content) throws ServiceException;

    boolean addGame(SessionRequestContent content) throws ServiceException;

    void addGameToShoppingCart(SessionRequestContent content) throws ServiceException;

    void deleteGameFromShoppingCart(SessionRequestContent content);

    boolean updateGame(SessionRequestContent content) throws ServiceException;

    boolean updateGenres(SessionRequestContent content) throws ServiceException;

    boolean deleteGame(SessionRequestContent content) throws  ServiceException;
}
