package com.fairycompany.reviewer.controller.command.impl.user;

import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class DeleteGameShoppingCart extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        GameService gameService = GameServiceImpl.getInstance();

        gameService.deleteGameFromShoppingCart(content);

        return router;
    }
}
