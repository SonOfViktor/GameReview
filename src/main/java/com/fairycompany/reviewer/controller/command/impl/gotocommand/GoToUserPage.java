package com.fairycompany.reviewer.controller.command.impl.gotocommand;

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

public class GoToUserPage implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.CURRENT_PAGE, PagePath.USER_PAGE_REDIRECT);
        Router router = new Router(PagePath.USER_PAGE);

        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();

        try {
            gameRatingService.findUserRatingAmount(content);
            content.insertValues(request);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finding user's amount of ratings is failed. {}", e.getMessage());
            throw new CommandException("Finding user's amount of ratings is failed", e);
        }

        return router;
    }
}
