package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.GameService;
import com.fairycompany.reviewer.model.service.impl.GameServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class DeleteGameCommand extends AbstractCommand {
    private static final String EMPTY_LINE = "";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        content.extractValues(request);
        String gameManagerRedirect = PagePath.GAME_MANAGER_REDIRECT + content.getRequestParameter(RequestParameter.ACTUAL_PAGE);
        Router router = new Router(gameManagerRedirect);
        router.setType(Router.RouterType.REDIRECT);

        String uploadDirectory = request.getServletContext().getRealPath(EMPTY_LINE);
        content.addRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY, uploadDirectory);

        GameService gameService = GameServiceImpl.getInstance();

        try {
            if(!gameService.deleteGame(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ILLEGAL_USE_ADDRESS_BAR);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when deleting game {}", e.getMessage());
            throw new CommandException("Some error when deleting game", e);
        }
        content.insertValues(request);

        return router;
    }
}
