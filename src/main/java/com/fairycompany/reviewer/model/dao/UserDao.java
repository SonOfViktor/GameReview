package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.User;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserDao extends BaseDao<User> {

    Optional<User> findByLoginAndPassword(String login, String password) throws DaoException;

    Optional<User> findUserByLogin(String login) throws DaoException;

    long findTotalUserAmount() throws DaoException;

    BigDecimal findUserBalance(long userId) throws DaoException;

    long add(User user, String password) throws DaoException;

    boolean updatePassword(long userId, String password) throws DaoException;

    boolean updateStatus(long id, User.Status status) throws DaoException;

    boolean updateRoleAndStatus(long userId, int userRole, int userStatus) throws DaoException;

    boolean updateUserBalance(long userId, BigDecimal newUserBalance) throws DaoException;
}
