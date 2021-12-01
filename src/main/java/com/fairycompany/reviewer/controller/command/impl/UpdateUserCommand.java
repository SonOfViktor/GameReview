package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.Command;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdateUserCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        String currentPage = (String) content.getSessionAttribute(SessionAttribute.CURRENT_PAGE);
        Router router = new Router(currentPage);
        router.setType(Router.RouterType.REDIRECT);

        UserService userService = UserServiceImpl.getInstance();

        try {
            userService.updateUser(content);
            content.insertValues(request);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when updating user data ", e);
            throw new CommandException("Some error when updating user data", e);
        }

        return router;
    }
}
