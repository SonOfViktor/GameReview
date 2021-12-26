package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class DeleteGameCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        content.extractValues(request);
        String gameManagerRedirect = PagePath.GAME_MANAGER_REDIRECT + content.getRequestParameter(RequestParameter.ACTUAL_PAGE);
        Router router = new Router(gameManagerRedirect);
        router.setType(Router.RouterType.REDIRECT);

        GameService gameService = GameServiceImpl.getInstance();

        try {
            gameService.deleteGame(content);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when deleting game {}", e.getMessage());
            throw new CommandException("Some error when deleting game", e);
        }
        content.insertValues(request);

        return router;
    }
}
