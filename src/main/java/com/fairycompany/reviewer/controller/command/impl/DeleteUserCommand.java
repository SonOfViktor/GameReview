package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class DeleteUserCommand extends AbstractCommand {
    private static final String EMPTY_LINE = "";

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        content.extractValues(request);
        Router router = new Router(PagePath.LOGOUT_REDIRECT);

        String uploadDirectory = request.getServletContext().getRealPath(EMPTY_LINE);
        content.addRequestAttribute(RequestAttribute.UPLOAD_DIRECTORY, uploadDirectory);

        UserService userService = UserServiceImpl.getInstance();

        try {
            userService.deleteUser(content);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when deleting user. {}", e.getMessage());
            throw new CommandException("Some error when deleting user", e);
        }
        content.insertValues(request);

        return router;
    }
}
