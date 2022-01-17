package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Entity;
import java.util.List;
import java.util.Optional;

public interface BaseDao <T extends Entity> {

    List<T> findAll(long skippedUsers, int rowAmount) throws DaoException;

    Optional<T> findEntityById(long id) throws DaoException;

    boolean delete(long id) throws DaoException;

    long add(T t) throws DaoException;

    boolean update(T t) throws DaoException;
}