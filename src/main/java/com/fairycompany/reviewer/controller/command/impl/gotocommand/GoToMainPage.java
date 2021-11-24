package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class GoToMainPage implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.CURRENT_PAGE, PagePath.MAIN_PAGE_REDIRECT);
        Router router = new Router(PagePath.MAIN_PAGE);

        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        GameService gameService = GameServiceImpl.getInstance();

        try {
            List<Map<String, Object>> games = gameService.findAllGamesForMainPage(content);
            session.setAttribute(SessionAttribute.GAME_LIST, games);
            content.insertValues(request);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finding games is failed. {}", e.getMessage());
            throw new CommandException("Finding games is failed", e);
        }

        return router;
    }
}
