package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.Router;
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
            gameRatingService.deleteUserReview(content);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Deleting comment is failed. {}", e.getMessage());
            throw new CommandException("Deleting comment is failed", e);
        }

        return router;
    }
}
