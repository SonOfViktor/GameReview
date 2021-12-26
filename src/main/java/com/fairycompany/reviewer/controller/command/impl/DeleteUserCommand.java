package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class DeleteUserCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        content.extractValues(request);
        Router router = new Router(PagePath.LOGOUT_REDIRECT);

        UserService userService = UserServiceImpl.getInstance();

        try {
            userService.deleteUser(content);
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.USER_DELETED);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when deleting user. {}", e.getMessage());
            throw new CommandException("Some error when deleting user", e);
        }
        content.insertValues(request);

        return router;
    }
}
