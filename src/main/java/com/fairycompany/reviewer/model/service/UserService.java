package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAllUsers(SessionRequestContent content) throws ServiceException;

    Optional<User> authenticate (SessionRequestContent content) throws ServiceException;

    boolean addUser(SessionRequestContent content) throws ServiceException;

    boolean updateUser(SessionRequestContent content) throws ServiceException;

    boolean updateUserByAdmin(SessionRequestContent content) throws ServiceException;

    boolean updatePassword(SessionRequestContent content) throws ServiceException;

    boolean deleteUser(SessionRequestContent content) throws ServiceException;

    boolean finishRegistration(SessionRequestContent content) throws ServiceException;
}
