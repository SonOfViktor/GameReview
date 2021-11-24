package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateGameUpdateRatingCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        String currentPage = (String)content.getSessionAttribute(SessionAttribute.CURRENT_PAGE);
        Router router = new Router(currentPage);
        router.setType(Router.RouterType.REDIRECT);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();
        try {
            if (!gameRatingService.addUpdateGameReview(content)) {
                session.setAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ADD_GAME_RATING_FAILED);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Adding rating is failed. {}", e.getMessage());
            throw new CommandException("Adding rating is failed", e);
        }

        return router;
    }
}
