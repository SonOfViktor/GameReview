package com.fairycompany.reviewer.model.dao.impl;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.UserDao;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
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
    private static final String SET_PASSWORD_SQL = """
            UPDATE users SET password = ?
            WHERE user_id = ?
            """;
    private static final String UPDATE_USER_SQL = """
            UPDATE users
            SET login = ?, first_name = ?, second_name = ?, birthday_date = ?, phone = ?, balance = ?, photo = ?,
            role_id = ?, status_id = ?
            WHERE user_id = ?
            """;


    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        return instance;
    }

    @Override
    public List<User> findAll() throws DaoException {
        List<User> users;

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_SQL)){

            users = createUser(resultSet);

        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding all users. {}", e.getMessage());
            throw new DaoException("Error when finding all users", e);
        }
        return users;
    }

    @Override
    public Optional<User> findEntityById(long id) throws DaoException {
        Optional<User> user = Optional.empty();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> users = createUser(resultSet);
                if (!users.isEmpty()) {
                    user = Optional.of(users.get(0));
                }
                else {
                    logger.log(Level.WARN, "User with Id {} isn't found", id);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding user with Id {}. {}", id, e.getMessage());
            throw new DaoException("Error when finding user with Id " + id, e);
        }

        return user;
    }

    public Optional<User> findByLoginAndPassword(String login, String password) throws DaoException {
        Optional<User> user = Optional.empty();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_LOGIN_AND_PASSWORD_SQL)) {
            statement.setString(1, login);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> users = createUser(resultSet);
                if (!users.isEmpty()) {
                    user = Optional.of(users.get(0));
                }
                else {
                    logger.log(Level.WARN, "User with login {} and password {} isn't found", login, password);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when finding user with login {} and password {}. {}", login, password, e.getMessage());
            throw new DaoException("Error when finding user with login " + login + " and password " + password, e);
        }

        return user;
    }

    @Override
    public boolean delete(long id) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setLong(1, id);
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when deleting user with Id {}. {}", id, e.getMessage());
            throw new DaoException("Error when deleting user with Id " + id, e);
        }
        return true;
    }

    @Override
    public boolean add(User user) throws DaoException {
        return false;
    }

    @Override
    public boolean add(User user, String password) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_NEW_USER_SQL)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, password);
            statement.setString(3, user.getFirstName());
            statement.setString(4, user.getSecondName());
            statement.setDate(5, Date.valueOf(user.getBirthday()));
            statement.setInt(6, user.getPhone());
            statement.setBigDecimal(7, user.getBalance());
            statement.setBlob(8, user.getPhoto());
            statement.setInt(9, user.getUserRole().ordinal());
            statement.setInt(10, user.getUserStatus().ordinal());
            System.out.println(statement.executeUpdate());
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when adding user. {}", e.getMessage());
            throw new DaoException("Error when adding user", e);
        }

        return true;
    }

    public boolean updatePassword(User user, String password) throws DaoException {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SET_PASSWORD_SQL)) {
            statement.setString(1, password);
            statement.setLong(2, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Error when updating password. {}", e.getMessage());
            throw new DaoException("Error when updating password user", e);
        }

        return true;
    }

    @Override
    public boolean update(User user) throws DaoException {
        return false;
    }

    private List<User> createUser(ResultSet userResultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (userResultSet.next()) {
            User user = new User.UserBuilder()
                    .setUserId(userResultSet.getLong(USER_ID))
                    .setLogin(userResultSet.getString(LOGIN))
                    .setFirstName(userResultSet.getString(FIRST_NAME))
                    .setSecondName(userResultSet.getString(SECOND_NAME))
                    .setBirthday(userResultSet.getDate(BIRTHDAY_DATE).toLocalDate())
                    .setPhone(userResultSet.getInt(PHONE))
                    .setBalance(userResultSet.getBigDecimal(BALANCE))
                    .setPhoto(userResultSet.getBlob(PHOTO))
                    .setUserRole(User.Role.valueOf(userResultSet.getString(ROLE).toUpperCase()))
                    .setUserStatus(User.Status.valueOf(userResultSet.getString(STATUS).toUpperCase()))
                    .createUser();
            users.add(user);
        }
        return users;
    }

}
