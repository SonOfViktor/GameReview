package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.User;

public interface UserDao extends BaseDao<User> {
    long add(User user, String password) throws DaoException;

    boolean updatePassword(User user, String password) throws DaoException;

    boolean updateStatus(long id, User.Status status) throws DaoException;
}
