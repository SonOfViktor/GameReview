package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String SOURCE_LINK_FORMAT = "%s://%s:%s/%s";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        String currentPage = (String)content.getSessionAttribute(SessionAttribute.CURRENT_PAGE);
        Router router = new Router(currentPage);
        router.setType(Router.RouterType.REDIRECT);             // todo to check

        String sourceLink = String.format(SOURCE_LINK_FORMAT, request.getScheme(), request.getServerName(),
                request.getServerPort(), request.getContextPath());
        content.addRequestAttribute(RequestAttribute.SOURCE_LINK, sourceLink);

        UserService userService = UserServiceImpl.getInstance();

        try {
            if (userService.addUser(content)) {
                router.setPage(PagePath.MAIN_PAGE_REDIRECT);
            } else {
                content.insertValues(request);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Adding user is failed. {}", e.getMessage());
            throw new CommandException("Adding user is failed", e);
        }

        return router;
    }
}
