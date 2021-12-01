package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> authenticate (SessionRequestContent content) throws ServiceException;

    boolean addUser(SessionRequestContent content) throws ServiceException;

    boolean updateUser(SessionRequestContent content) throws ServiceException;

    boolean updatePassword(SessionRequestContent content) throws ServiceException;

    boolean updatePhoto(SessionRequestContent content) throws ServiceException;

    boolean finishRegistration(SessionRequestContent content) throws ServiceException;
}
