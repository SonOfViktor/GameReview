package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.Optional;

public class CreateGameCommand extends AbstractCommand {
    private static final String RELATIVE_IMAGE_PATH = "media\\game";
    private static final String DEFAULT_FILE = "pic\\default_game.jpg";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        String uploadDirectory = request.getServletContext().getRealPath(RELATIVE_IMAGE_PATH);

        Optional<String> imageName = saveImage(request, uploadDirectory, DEFAULT_FILE);
        if (imageName.isPresent()) {
            content.addRequestAttribute(RequestAttribute.GAME_IMAGE,
                    RELATIVE_IMAGE_PATH + File.separator + imageName.get());

            GameService gameService = GameServiceImpl.getInstance();

            try {
                if (gameService.addGame(content)) {
                    content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.ADD_GAME_SUCCESSFUL);
                } else {
                    content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ADD_GAME_FAILED);
                    deleteFile(uploadDirectory + File.separator + imageName.get());
                }
            } catch (ServiceException e) {
                logger.log(Level.ERROR, "Adding game is failed. {}", e.getMessage());
                deleteFile(uploadDirectory + File.separator + imageName.get());
                throw new CommandException("Adding game is failed", e);
            }
        }
        content.insertValues(request);

        return router;
    }
}
