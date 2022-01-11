package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.User;

import java.math.BigDecimal;

public interface UserDao extends BaseDao<User> {
    BigDecimal findUserBalance(long userId) throws DaoException;

    long add(User user, String password) throws DaoException;

    boolean updatePassword(long userId, String password) throws DaoException;

    boolean updateStatus(long id, User.Status status) throws DaoException;

    boolean updateUserBalance(long userId, BigDecimal newUserBalance) throws DaoException;
}
