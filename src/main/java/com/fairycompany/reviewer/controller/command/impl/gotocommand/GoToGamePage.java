package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.GameRating;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.GameRatingService;
import com.fairycompany.reviewer.model.service.impl.GameRatingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class GoToGamePage implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.CURRENT_PAGE, (PagePath.GAME_PAGE_REDIRECT + "&game_id=" +
                request.getParameter(RequestParameter.GAME_ID)));       // todo поправить
        Router router = new Router(PagePath.GAME_PAGE);

        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        long gameId = Long.parseLong(request.getParameter(RequestParameter.GAME_ID));
        List<Map<String, Object>> gameList = (List<Map<String, Object>>)session.getAttribute(SessionAttribute.GAME_LIST);

        Map<String, Object> game = gameList.stream()                            // todo util
                .filter(t -> Game.class.cast(t.get("game")).getGameId() == gameId)
                .findFirst()
                .get();
        request.setAttribute(RequestAttribute.GAME_MAP, game);

        GameRatingService gameRatingService = GameRatingServiceImpl.getInstance();

        try {
            User user = (User) session.getAttribute(SessionAttribute.USER);

                GameRating rating = gameRatingService.findGameRating(content);
                request.setAttribute(RequestAttribute.USER_RATING, rating);
                content.insertValues(request);

        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Game page is not available. {}", e.getMessage());
            throw new CommandException("Game page is not available.", e);
        }

        return router;
    }
}
