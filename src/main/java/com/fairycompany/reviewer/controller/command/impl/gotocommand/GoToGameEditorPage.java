package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.Optional;

public class GoToGameEditorPage extends AbstractCommand {
    private static final String EMPTY_LINE = "";
    private static final String EMBED = "embed/";
    private static final String WATCH = "watch?v=";
    private static final String AUTOPLAY = "?&autoplay=1";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        GameService gameService = GameServiceImpl.getInstance();

        try {
            Optional<Game> optionalGame = gameService.findGame(content);
            if (optionalGame.isPresent()) {
                Game game = optionalGame.get();
                convertTrailerUrl(game);

                content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, PagePath.GAME_EDITOR_PAGE_REDIRECT +
                        request.getParameter(RequestParameter.GAME_ID));
                router = new Router(PagePath.GAME_EDITOR_PAGE);

                content.addRequestAttribute(RequestAttribute.GAME, game);
                content.addRequestAttribute(RequestAttribute.GENRES, Game.Genre.values());
                content.addRequestAttribute(RequestAttribute.PLATFORMS, Platform.values());
            } else {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ILLEGAL_USE_ADDRESS_BAR);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when finding game. {}", e.getMessage());
            throw new CommandException("Some error when finding game", e);
        }
        content.insertValues(request);

        return router;
    }

    private void convertTrailerUrl(Game game) {
        String youtubeUrlRaw = game.getTrailerUrl();
        String youtubeUrl = youtubeUrlRaw.replace(EMBED, WATCH).replace(AUTOPLAY, EMPTY_LINE);
        game.setTrailerUrl(youtubeUrl);
    }
}
