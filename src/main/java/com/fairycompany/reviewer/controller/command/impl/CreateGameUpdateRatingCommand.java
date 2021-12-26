package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class CreateGameUpdateRatingCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();
        try {
            if (!gameRatingService.addUpdateGameReview(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ADD_GAME_RATING_FAILED);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Adding rating is failed. {}", e.getMessage());
            throw new CommandException("Adding rating is failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
