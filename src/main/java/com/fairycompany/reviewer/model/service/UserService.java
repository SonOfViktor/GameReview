package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * The interface provide methods to control business logic related with payments.
 */
public interface UserService {
    /**
     * Find all users.
     *
     * @param content request content
     * @return list with all users
     * @throws ServiceException when DaoException occurred
     */
    List<User> findAllUsers(SessionRequestContent content) throws ServiceException;

    /**
     * Authenticate user.
     *
     * @param content request content
     * @return optional with user with certain login and password or empty optional if user was not found
     * @throws ServiceException when DaoException occurred
     */
    Optional<User> authenticateUser(SessionRequestContent content) throws ServiceException;

    /**
     * Add user to data-base.
     *
     * @param content request content
     * @return true if user was added
     * @throws ServiceException when DaoException occurred
     */
    boolean addUser(SessionRequestContent content) throws ServiceException;

    /**
     * Update user's data.
     *
     * @param content request content
     * @return true if user's data was updated
     * @throws ServiceException when DaoException occurred
     */
    boolean updateUser(SessionRequestContent content) throws ServiceException;

    /**
     * Change status and / or role of user.
     *
     * @param content request content
     * @return true if status and / or role was changed
     * @throws ServiceException when DaoException occurred
     */
    boolean updateUserByAdmin(SessionRequestContent content) throws ServiceException;

    /**
     * Update user's password.
     *
     * @param content request content
     * @return true if user's password was updated
     * @throws ServiceException when DaoException occurred
     */
    boolean updatePassword(SessionRequestContent content) throws ServiceException;

    /**
     * Delete user from data-base.
     *
     * @param content request content
     * @return true if user was deleted
     * @throws ServiceException when DaoException occurred
     */
    boolean deleteUser(SessionRequestContent content) throws ServiceException;

    /**
     * Finish registration of user.
     *
     * @param content request content
     * @return return true if registration was successful
     * @throws ServiceException when DaoException occurred
     */
    boolean finishRegistration(SessionRequestContent content) throws ServiceException;
}
