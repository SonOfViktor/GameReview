package com.fairycompany.reviewer.model.dao.mapper.impl;

import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.UserToken;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class TokenResultSetHandler implements ResultSetHandler<UserToken> {
    @Override
    public UserToken resultSetToObject(ResultSet resultSet) throws SQLException {
        long userId = resultSet.getLong(USER_ID);
        String token = resultSet.getString(TOKEN);
        Timestamp creationDate = resultSet.getTimestamp(TOKEN_CREATION_DATE);
        return new UserToken(userId, token, creationDate.toLocalDateTime());
    }
}
