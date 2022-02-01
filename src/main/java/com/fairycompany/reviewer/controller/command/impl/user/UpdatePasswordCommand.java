package com.fairycompany.reviewer.controller.command.impl.user;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class UpdatePasswordCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        UserService userService = UserServiceImpl.getInstance();

        try {
            if (userService.updatePassword(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.PASSWORD_UPDATED);
                logger.log(Level.DEBUG, "Password updated");
            } else {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.UPDATE_PASSWORD_FAILED);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when updating user password {}", e.getMessage());
            throw new CommandException("Some error when updating user password", e);
        }
        content.insertValues(request);

        return router;
    }
}
