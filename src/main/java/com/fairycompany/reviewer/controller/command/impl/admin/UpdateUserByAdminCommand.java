package com.fairycompany.reviewer.controller.command.impl.admin;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class UpdateUserByAdminCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        UserService userService = UserServiceImpl.getInstance();

        try {
            if (!userService.updateUserByAdmin(content)) {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.ILLEGAL_USE_ADDRESS_BAR);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some error when updating user data {}", e.getMessage());
            throw new CommandException("Some error when updating user data", e);
        }
        content.insertValues(request);

        return router;
    }
}
