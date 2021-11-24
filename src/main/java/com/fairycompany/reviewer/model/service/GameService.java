package com.fairycompany.reviewer.model.service;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;

import java.util.List;
import java.util.Map;

public interface GameService {

    List<Map<String, Object>> findAllGamesForMainPage(SessionRequestContent content) throws ServiceException;

    boolean addGame(SessionRequestContent content) throws ServiceException;
}
