package com.fairycompany.reviewer.controller.command.impl.admin;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class CreateGameCommand extends AbstractCommand {
    private static final String EMPTY_LINE = "";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        if (extractImageInputStream(request)) {
            String uploadDirectory = request.getServletContext().getRealPath(EMPTY_LINE);
            content.addRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY, uploadDirectory);

            GameService gameService = GameServiceImpl.getInstance();

            try {
                gameService.addGame(content);
            } catch (ServiceException e) {
                logger.log(Level.ERROR, "Adding game is failed. {}", e.getMessage());
                throw new CommandException("Adding game is failed", e);
            }
        }
        content.insertValues(request);

        return router;
    }
}
