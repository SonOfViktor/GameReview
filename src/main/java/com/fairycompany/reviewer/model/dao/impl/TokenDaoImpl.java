package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.mapper.impl.TokenResultSetHandler;
import com.fairycompany.reviewer.model.entity.UserToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

public class TokenDaoImpl {
    private static final Logger logger = LogManager.getLogger();
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

    private JdbcTemplate<UserToken> jdbcTemplate;

    private TokenDaoImpl() {
        jdbcTemplate = new JdbcTemplate<UserToken>(new TokenResultSetHandler());
    }

    public static TokenDaoImpl getInstance() {
        return instance;
    }

    public Optional<UserToken> findTokenById(long tokenId) throws DaoException {
        Optional<UserToken> userToken = jdbcTemplate.executeSelectQueryForObject(FIND_USER_TOKEN_SQL, tokenId);

        return userToken;
    }

    public long addRegistrationToken(long userId, String token) throws DaoException {
        long tokenId = jdbcTemplate.executeInsertQuery(ADD_USER_TOKEN_SQL, userId,
                token, Timestamp.valueOf(LocalDateTime.now()));

        return tokenId;
    }

}
