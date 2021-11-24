package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.GameService;

public class _MainService {

    public static void main(String[] args) throws ServiceException {
        UserServiceImpl userService = UserServiceImpl.getInstance();
//        User user = userService.authenticate("punksim@mail.ru", "zxc123asd").get();
//        System.out.println(user);
        SessionRequestContent content = new SessionRequestContent();

        GameService gameService = GameServiceImpl.getInstance();
        gameService.findAllGamesForMainPage(content);
    }

}
