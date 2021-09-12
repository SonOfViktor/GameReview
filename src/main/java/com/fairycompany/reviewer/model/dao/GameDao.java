package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Game;

import java.util.Optional;

public interface GameDao extends BaseDao<Game> {
    Optional<Game> findGameByName(String name) throws DaoException;
}
