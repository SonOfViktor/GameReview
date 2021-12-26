package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class GoToGameEditorPage implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.CURRENT_PAGE, (PagePath.GAME_EDITOR_PAGE_REDIRECT +
                request.getParameter(RequestParameter.GAME_ID)));
        Router router = new Router(PagePath.GAME_EDITOR_PAGE);

        long gameId = Long.parseLong(request.getParameter(RequestParameter.GAME_ID));

        GameService gameService = GameServiceImpl.getInstance();

        try {
            Game game = gameService.findGame(gameId).get();
            convertTrailerUrl(game);
            request.setAttribute(RequestAttribute.GAME, game);
            request.setAttribute(RequestAttribute.GENRES, Game.Genre.values());
            request.setAttribute(RequestAttribute.PLATFORMS, Platform.values());
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when finding game. {}", e.getMessage());
            throw new CommandException("Some error when finding game", e);
        }

        return router;
    }

    private void convertTrailerUrl(Game game) {
        //todo String constant
        String youtubeUrlRaw = game.getTrailerUrl();
        String youtubeUrl = youtubeUrlRaw.replace("embed/", "watch?v=").replace("?&autoplay=1", "");
        game.setTrailerUrl(youtubeUrl);
    }
}
