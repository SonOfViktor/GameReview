package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class GoToUserPage extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        content.extractValues(request);
        content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, PagePath.USER_PAGE_REDIRECT);
        Router router = new Router(PagePath.USER_PAGE);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();

        try {
            gameRatingService.findUserRatingAmount(content);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finding user's amount of ratings is failed. {}", e.getMessage());
            throw new CommandException("Finding user's amount of ratings is failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
