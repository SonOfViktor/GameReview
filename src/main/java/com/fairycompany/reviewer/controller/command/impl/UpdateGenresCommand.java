package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class UpdateGenresCommand extends AbstractCommand{

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        GameService gameService = GameServiceImpl.getInstance();

        try {
            if (!gameService.updateGenres(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.UPDATE_GENRE_FAILED);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when updating genres {}", e.getMessage());
            throw new CommandException("Some error when updating genres", e);
        }
        content.insertValues(request);

        return router;
    }
}
