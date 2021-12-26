package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class UpdateGameImageCommand extends AbstractCommand{

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        long gameId = Long.parseLong(request.getParameter(RequestParameter.GAME_ID));

        GameService gameService = GameServiceImpl.getInstance();

        try {
            Game game = gameService.findGame(gameId).get();
            String relativeImagePath = game.getImage();
            String imagePath = request.getServletContext().getRealPath(relativeImagePath);
            updateImage(request, imagePath);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when updating image. {}", e.getMessage());
            throw new CommandException("Some error when updating image", e);
        }
        content.insertValues(request);

        return router;
    }
}
