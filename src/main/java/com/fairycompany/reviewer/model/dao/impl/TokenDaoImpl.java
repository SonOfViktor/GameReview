package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.TokenDao;
import com.fairycompany.reviewer.model.dao.mapper.impl.TokenResultSetHandler;
import com.fairycompany.reviewer.model.entity.UserToken;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class TokenDaoImpl implements TokenDao {
    private static TokenDaoImpl instance = new TokenDaoImpl();

    private static final String ADD_USER_TOKEN_SQL = """
            INSERT INTO tokens (user_id, token, create_date)
            VALUES (?, ?, ?)
            """;
    private static final String FIND_USER_TOKEN_SQL = """
            SELECT user_id, token, create_date
            FROM tokens
            WHERE token_id = ?
            """;
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM tokens WHERE token_id = ?
            """;

    private JdbcTemplate<UserToken> jdbcTemplate;

    private TokenDaoImpl() {
        jdbcTemplate = new JdbcTemplate<>(new TokenResultSetHandler());
    }

    public static TokenDaoImpl getInstance() {
        return instance;
    }

    @Override
    public Optional<UserToken> findTokenById(long tokenId) throws DaoException {
        Optional<UserToken> userToken = jdbcTemplate.selectEntity(FIND_USER_TOKEN_SQL, tokenId);

        return userToken;
    }

    @Override
    public long addRegistrationToken(long userId, String token) throws DaoException {
        long tokenId = jdbcTemplate.insertDataInTable(ADD_USER_TOKEN_SQL, userId,
                token, Timestamp.valueOf(LocalDateTime.now()));

        return tokenId;
    }

    @Override
    public boolean deleteToken(long tokenId) throws DaoException {
        boolean isDeleted = jdbcTemplate.updateDeleteFields(DELETE_BY_ID_SQL, tokenId);

        return isDeleted;
    }

}
