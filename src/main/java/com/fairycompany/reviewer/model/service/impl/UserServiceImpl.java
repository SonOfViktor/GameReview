package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.impl.UserDaoImpl;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.util.HashGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    public static final Logger logger = LogManager.getLogger();
    private UserDaoImpl userDao = UserDaoImpl.getInstance();


    public Optional<User> authenticate (String login, String password) throws ServiceException {
        Optional<User> user = Optional.empty();
        String hashPassword = HashGenerator.hashPassword(password);
        try {
            user = userDao.findByLoginAndPassword(login, hashPassword);
        } catch (DaoException e) {
            logger.log(Level.ERROR, "Error when authenticating user with login {} and password {}. {}", login, password, e.getMessage());
            throw new ServiceException("Error when authenticating user with login " + login + " and password " + password, e);
        }
        return user;
    }
}
