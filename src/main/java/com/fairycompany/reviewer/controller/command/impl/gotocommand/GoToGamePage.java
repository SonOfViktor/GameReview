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

import java.util.Optional;

public class GoToGamePage extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        GameService gameService = GameServiceImpl.getInstance();
        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();

        try {
            Optional<Game> optionalGame = gameService.findGame(content);
            Optional<GameRating> optionalRating = gameRatingService.findGameRating(content);
            if (optionalGame.isPresent() && optionalRating.isPresent()) {
                Game game = optionalGame.get();
                request.setAttribute(RequestAttribute.GAME, game);
                GameRating rating = optionalRating.get();
                request.setAttribute(RequestAttribute.USER_RATING, rating);

                String gamePage = String.format(PagePath.GAME_PAGE_REDIRECT, request.getParameter(RequestParameter.GAME_ID),
                        request.getParameter(RequestParameter.ACTUAL_PAGE));

                content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, gamePage);
                router = new Router(PagePath.GAME_PAGE);
            } else {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ILLEGAL_USE_ADDRESS_BAR);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Game page is not available. {}", e.getMessage());
            throw new CommandException("Game page is not available.", e);
        }
        content.insertValues(request);

        return router;
    }
}
