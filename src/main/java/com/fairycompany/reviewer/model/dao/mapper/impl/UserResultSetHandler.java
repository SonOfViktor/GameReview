package com.fairycompany.reviewer.model.dao.mapper.impl;

import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class UserResultSetHandler implements ResultSetHandler<User> {

    @Override
    public User resultSetToObject(ResultSet resultSet) throws SQLException {
        User user = new User.UserBuilder()
                .setUserId(resultSet.getLong(USER_ID))
                .setLogin(resultSet.getString(LOGIN))
                .setFirstName(resultSet.getString(FIRST_NAME))
                .setSecondName(resultSet.getString(SECOND_NAME))
                .setBirthday(resultSet.getDate(BIRTHDAY_DATE).toLocalDate())
                .setPhone(resultSet.getInt(PHONE))
                .setPhoto(resultSet.getString(PHOTO))
                .setUserRole(User.Role.valueOf(resultSet.getString(ROLE).toUpperCase()))
                .setUserStatus(User.Status.valueOf(resultSet.getString(STATUS).toUpperCase()))
                .createUser();
        return user;
    }
}

