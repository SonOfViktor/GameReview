package com.fairycompany.reviewer.controller.command.impl.user;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class CreateRatingCommand extends AbstractCommand {
    private static final String CREATE = "create";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);
        content.addRequestAttribute(RequestAttribute.SWITCH, CREATE);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();
        try {
            if (!gameRatingService.addUpdateGameReview(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ADD_GAME_RATING_FAILED);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Add rating is failed. {}", e.getMessage());
            throw new CommandException("Add rating is failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
