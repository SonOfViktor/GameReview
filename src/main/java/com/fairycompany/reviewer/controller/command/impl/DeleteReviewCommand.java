package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class DeleteReviewCommand extends AbstractCommand{

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();

        try {
            if (!gameRatingService.deleteUserReview(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ILLEGAL_USE_ADDRESS_BAR);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Deleting comment is failed. {}", e.getMessage());
            throw new CommandException("Deleting comment is failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
