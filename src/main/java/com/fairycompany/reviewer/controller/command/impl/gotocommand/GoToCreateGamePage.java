package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Platform;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class GoToCreateGamePage implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        session.setAttribute(SessionAttribute.CURRENT_PAGE, PagePath.CREATE_GAME_REDIRECT);
        request.setAttribute(RequestAttribute.GENRES, Game.Genre.values());
        request.setAttribute(RequestAttribute.PLATFORMS, Platform.values());

        Router router = new Router(PagePath.CREATE_GAME);
        return router;
    }
}
