package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.UserDao;
import com.fairycompany.reviewer.model.dao.mapper.impl.UserResultSetHandler;
import com.fairycompany.reviewer.model.entity.User;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;


public class UserDaoImpl implements UserDao {
    private static final Logger logger = LogManager.getLogger();
    private static UserDaoImpl instance = new UserDaoImpl();
    private static final String FIND_ALL_SQL = """
            SELECT user_id, login, first_name, second_name, birthday_date, phone, balance, photo, role, status
            FROM users
            JOIN roles ON users.role_id = roles.role_id
            JOIN statuses ON users.status_id = statuses.status_id
            ORDER BY user_id
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT user_id, login, first_name, second_name, birthday_date, phone, balance, photo, role, status
            FROM users
            JOIN roles ON users.role_id = roles.role_id
            JOIN statuses ON users.status_id = statuses.status_id
            WHERE user_id = ?
            """;
    private static final String FIND_BY_LOGIN_AND_PASSWORD_SQL = """
            SELECT user_id, login, first_name, second_name, birthday_date, phone, balance, photo, role, status
            FROM users
            JOIN roles ON users.role_id = roles.role_id
            JOIN statuses ON users.status_id = statuses.status_id
            WHERE login = ? AND password = ?
            """;
    private static final String DELETE_BY_ID_SQL = """
            DELETE FROM users WHERE user_id = ?
            """;
    private static final String ADD_NEW_USER_SQL = """
            INSERT INTO users (login, password, first_name, second_name, birthday_date, phone, balance, photo,
            role_id, status_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    private static final String UPDATE_PASSWORD_SQL = """
            UPDATE users SET password = ?
            WHERE user_id = ?
            """;
    private static final String UPDATE_STATUS_SQL = """
            UPDATE users SET status_id = ?
            WHERE user_id = ?
            """;
    private static final String UPDATE_USER_SQL = """
            UPDATE users
            SET login = ?, first_name = ?, second_name = ?, birthday_date = ?, phone = ?, balance = ?, photo = ?,
            role_id = ?, status_id = ?
            WHERE user_id = ?
            """;


    private JdbcTemplate<User> jdbcTemplate;

    private UserDaoImpl() {
        jdbcTemplate = new JdbcTemplate<>(new UserResultSetHandler());
    }

    public static UserDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users = jdbcTemplate.executeSelectQuery(FIND_ALL_SQL);

        return users;
    }

    @Override
    public Optional<User> findEntityById(long id) throws DaoException {
        Optional<User> user = jdbcTemplate.executeSelectQueryForObject(FIND_BY_ID_SQL, id);

        return user;
    }

    public Optional<User> findByLoginAndPassword(String login, String password) throws DaoException {
        Optional<User> user = jdbcTemplate.executeSelectQueryForObject(FIND_BY_LOGIN_AND_PASSWORD_SQL, login, password);

        return user;
    }

    @Override
    public boolean delete(long id) throws DaoException {
        boolean isDeleted = jdbcTemplate.executeUpdateDeleteFields(DELETE_BY_ID_SQL, id);

        return isDeleted;
    }

    @Override
    public long add(User user) throws DaoException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long add(User user, String password) throws DaoException {
        long userId = jdbcTemplate.executeInsertQuery(ADD_NEW_USER_SQL,
                user.getLogin(),
                password,
                user.getFirstName(),
                user.getSecondName(),
                Date.valueOf(user.getBirthday()),
                user.getPhone(),
                user.getBalance(),
                user.getPhoto(),
                user.getUserRole().ordinal(),
                user.getUserStatus().ordinal());
        logger.log(Level.INFO, "User {} was added", user.getLogin());

        return userId;
    }

    public boolean updatePassword(User user, String password) throws DaoException {
        boolean isUpdated = jdbcTemplate.executeUpdateDeleteFields(UPDATE_PASSWORD_SQL, password, user.getUserId());

        return isUpdated;
    }

    public boolean updateStatus(long userId, User.Status status) throws DaoException {
        boolean isUpdated = jdbcTemplate.executeUpdateDeleteFields(UPDATE_STATUS_SQL, status.ordinal(), userId);

        return isUpdated;
    }

    @Override
    public boolean update(User user) throws DaoException {
        return false;
    }

}
