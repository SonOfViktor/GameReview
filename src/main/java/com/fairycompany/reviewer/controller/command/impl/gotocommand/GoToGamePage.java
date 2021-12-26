package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.GameRating;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class GoToGamePage extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String gamePage = String.format(PagePath.GAME_PAGE_REDIRECT, request.getParameter(RequestParameter.GAME_ID),
                request.getParameter(RequestParameter.ACTUAL_PAGE));

        content.extractValues(request);
        content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, gamePage);
        Router router = new Router(PagePath.GAME_PAGE);

        long gameId = Long.parseLong(content.getRequestParameter(RequestParameter.GAME_ID));

        GameService gameService = GameServiceImpl.getInstance();
        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();

        try {
            Game game = gameService.findGame(gameId).get();
            request.setAttribute(RequestAttribute.GAME, game);
            GameRating rating = gameRatingService.findGameRating(content);
            request.setAttribute(RequestAttribute.USER_RATING, rating);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Game page is not available. {}", e.getMessage());
            throw new CommandException("Game page is not available.", e);
        }
        content.insertValues(request);

        return router;
    }
}
