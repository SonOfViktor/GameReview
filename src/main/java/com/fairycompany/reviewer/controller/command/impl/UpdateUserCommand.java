package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class UpdateUserCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        UserService userService = UserServiceImpl.getInstance();

        try {
            userService.updateUser(content);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when updating user data {}", e.getMessage());
            throw new CommandException("Some error when updating user data", e);
        }
        content.insertValues(request);

        return router;
    }
}
