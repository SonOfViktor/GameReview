package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.GameRating;

public interface GameRatingService {
    GameRating findGameRating(SessionRequestContent content) throws ServiceException;

    boolean addUpdateGameReview(SessionRequestContent content) throws ServiceException;

    boolean findUserRatingAmount(SessionRequestContent content) throws ServiceException;

    boolean deleteUserRating(SessionRequestContent content) throws ServiceException;

    boolean deleteUserReview(SessionRequestContent content) throws ServiceException;

}
