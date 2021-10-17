package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private static final String SOURCE_LINK_FORMAT = "%s://%s:%s/%s";
    @Override
    public Router execute(HttpServletRequest request) {
        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        String currentPage = (String)content.getSessionAttribute(SessionAttribute.CURRENT_PAGE);
        Router router = new Router(currentPage);
        logger.log(Level.DEBUG, "Current page is {}",currentPage);

        String sourceLink = String.format(SOURCE_LINK_FORMAT, request.getScheme(), request.getServerName(),
                request.getServerPort(), request.getContextPath());
        content.addRequestAttribute(RequestAttribute.SOURCE_LINK, sourceLink);

        UserServiceImpl userService = UserServiceImpl.getInstance();

        try {
            if (userService.addUser(content)) {
                logger.log(Level.DEBUG, content.getSessionAttribute(SessionAttribute.USER));        //todo delete
                router.setPage(PagePath.MAIN_PAGE);
                router.setType(Router.RouterType.REDIRECT);
            } else {
                content.insertValues(request);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Adding user is failed", e);
        }

        return router;
    }
}
