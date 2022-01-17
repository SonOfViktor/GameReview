package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.UserToken;

import java.util.Optional;

public interface TokenDao {
    Optional<UserToken> findTokenById(long tokenId) throws DaoException;

    long addRegistrationToken(long userId, String token) throws DaoException;

    boolean deleteToken(long tokenId) throws DaoException;
}
