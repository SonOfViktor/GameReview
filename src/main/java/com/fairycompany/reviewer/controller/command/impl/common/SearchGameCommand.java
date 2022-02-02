package com.fairycompany.reviewer.controller.command.impl.common;

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

public class SearchGameCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String mainPageRedirect = String.format(PagePath.SEARCH_REDIRECT,
                request.getParameter(RequestParameter.ACTUAL_PAGE),
                request.getParameter(RequestParameter.SEARCH_FIELD));

        content.extractValues(request);
        content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, mainPageRedirect);
        Router router = new Router(PagePath.MAIN_PAGE);

        GameService gameService = GameServiceImpl.getInstance();

        try {
            List<Map<String, Object>> games = gameService.findSearchedGamesWithRating(content);
            content.addRequestAttribute(RequestAttribute.GAME_LIST, games);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finding games is failed. {}", e.getMessage());
            throw new CommandException("Finding games is failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
