package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Map;

public class GoToGameManagerPage extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String gameManagerRedirect = PagePath.GAME_MANAGER_REDIRECT + request.getParameter(RequestParameter.ACTUAL_PAGE);

        content.extractValues(request);
        content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, gameManagerRedirect);
        Router router = new Router(PagePath.GAME_MANAGER_PAGE);

        GameService gameService = GameServiceImpl.getInstance();
        try {
            List<Map<String, Object>> games = gameService.findAllGamesWithRating(content);
            content.addRequestAttribute(RequestAttribute.GAME_LIST, games);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finding games is failed. {}", e.getMessage());
            throw new CommandException("Finding games is failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
