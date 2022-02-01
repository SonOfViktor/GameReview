package com.fairycompany.reviewer.controller.command.impl.user;

import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class AddGameToShoppingCart extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        GameService gameService = GameServiceImpl.getInstance();

        try {
            gameService.addGameToShoppingCart(content);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when adding game to shopping cart. {}", e.getMessage());
            throw new CommandException("Some error when adding game to shopping cart.", e);
        }

        return router;
    }
}
