package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.UserToken;

import java.util.Optional;

/**
 * The interface provides methods to control data in database table related with tokens.
 */
public interface TokenDao {
    /**
     * Find token by id.
     *
     * @param tokenId token id
     * @return optional with token or empty optional if token was not found
     * @throws DaoException if SQL exception occurred
     */
    Optional<UserToken> findTokenById(long tokenId) throws DaoException;

    /**
     * Add registration token to database.
     *
     * @param userId user id
     * @param token  token
     * @return id for token
     * @throws DaoException if SQL exception occurred
     */
    long addRegistrationToken(long userId, String token) throws DaoException;

    /**
     * Delete specified token from database.
     *
     * @param tokenId token id
     * @return true if token was deleted
     * @throws DaoException if SQL exception occurred
     */
    boolean deleteToken(long tokenId) throws DaoException;
}
