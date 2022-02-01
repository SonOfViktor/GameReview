package com.fairycompany.reviewer.controller.command.impl.user;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class UpdateRatingCommand extends AbstractCommand {
    private static final String UPDATE = "update";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);
        content.addRequestAttribute(RequestAttribute.SWITCH, UPDATE);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();
        try {
            if (!gameRatingService.addUpdateGameReview(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.UPDATE_GAME_RATING_FAILED);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Update rating is failed. {}", e.getMessage());
            throw new CommandException("Update rating is failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
