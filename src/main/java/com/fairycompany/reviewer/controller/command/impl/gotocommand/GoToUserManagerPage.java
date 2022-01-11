package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.List;

public class GoToUserManagerPage extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String userManagerRedirect = PagePath.USER_MANAGER_REDIRECT + request.getParameter(RequestParameter.ACTUAL_PAGE);

        content.extractValues(request);
        content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, userManagerRedirect);
        Router router = new Router(PagePath.USER_MANAGER_PAGE);

        UserService userService = UserServiceImpl.getInstance();
        try {
            List<User> users = userService.findAllUsers(content);
            content.addRequestAttribute(RequestAttribute.USER_LIST, users);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finding users failed. {}", e.getMessage());
            throw new CommandException("Finding users failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
