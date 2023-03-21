package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Entity;
import java.util.List;
import java.util.Optional;

/**
 * The interface provided common methods for other dao interfaces.
 *
 * @param <T> type of class extends entity
 */
public interface BaseDao <T extends Entity> {

    /**
     * Find all entities with pagination.
     *
     * @param skipped the skipped rows of database
     * @param rowAmount row amount to extract
     * @return list with specified entities
     * @throws DaoException if SQL exception occurred
     */
    List<T> findAll(long skipped, int rowAmount) throws DaoException;

    /**
     * Find entity by specified id.
     *
     * @param id id of entity
     * @return optional with entity or empty optional if entity with the id doesn't exist
     * @throws {@link DaoException#DaoException()} if SQL exception occurred
     */
    Optional<T> findEntityById(long id) throws DaoException;

    /**
     * Add entity to database.
     *
     * @param t some object extends Entity
     * @return id for this object
     * @throws DaoException if SQL exception occurred
     */
    long add(T t) throws DaoException;

    /**
     * Update entity.
     *
     * @param t some object extends Entity
     * @return true if entity was updated
     * @throws DaoException if SQL exception occurred
     */
    boolean update(T t) throws DaoException;

    /**
     * Delete entity.
     *
     * @param id id of entity
     * @return true if entity was deleted
     * @throws DaoException if SQL exception occurred
     */
    boolean delete(long id) throws DaoException;
}