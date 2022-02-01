package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.User;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * The interface provides methods to control data in database table related with users.
 */
public interface UserDao extends BaseDao<User> {

    /**
     * Find user by login and password.
     *
     * @param login    user login
     * @param password user password
     * @return optional with user if user with specified login and passport exists in the database or empty optional
     * @throws DaoException if SQL exception occurred
     */
    Optional<User> findByLoginAndPassword(String login, String password) throws DaoException;

    /**
     * Find user by login.
     *
     * @param login user login
     * @return optional with user if user with specified login exists in the database or empty optional
     * @throws DaoException if SQL exception occurred
     */
    Optional<User> findUserByLogin(String login) throws DaoException;

    /**
     * Find total amount of users in database.
     *
     * @return total amount of users
     * @throws DaoException if SQL exception occurred
     */
    long findTotalUserAmount() throws DaoException;

    /**
     * Find user's balance.
     *
     * @param userId user id
     * @return balance of specified user
     * @throws DaoException if SQL exception occurred
     */
    BigDecimal findUserBalance(long userId) throws DaoException;

    /**
     * Add user to database.
     *
     * @param user     user
     * @param password password
     * @return id for new user
     * @throws DaoException if SQL exception occurred
     */
    long add(User user, String password) throws DaoException;

    /**
     * Update user's password.
     *
     * @param userId   user id
     * @param password password
     * @return true if password was updated
     * @throws DaoException if SQL exception occurred
     */
    boolean updatePassword(long userId, String password) throws DaoException;

    /**
     * Update user's status.
     *
     * @param id     user id
     * @param status status
     * @return true if status was updated
     * @throws DaoException if SQL exception occurred
     */
    boolean updateStatus(long id, User.Status status) throws DaoException;

    /**
     * Update role and status of specified user.
     *
     * @param userId     user id
     * @param userRole   user role
     * @param userStatus user status
     * @return true if role and status was updated
     * @throws DaoException if SQL exception occurred
     */
    boolean updateRoleAndStatus(long userId, int userRole, int userStatus) throws DaoException;

    /**
     * Update user balance.
     *
     * @param userId         user id
     * @param newUserBalance new user balance
     * @return true if user balance was updated
     * @throws DaoException if SQL exception occurred
     */
    boolean updateUserBalance(long userId, BigDecimal newUserBalance) throws DaoException;
}
