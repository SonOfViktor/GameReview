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

public class FinishRegistrationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router = new Router(PagePath.MAIN_PAGE_REDIRECT);
        router.setType(Router.RouterType.REDIRECT);

        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        UserService userService = UserServiceImpl.getInstance();

        try {
            if (userService.finishRegistration(content)) {
                request.getSession().setAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.REGISTRATION_SUCCESSFUL);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finishing registration failed. {}", e.getMessage());
            throw new CommandException("Finishing registration failed", e);
        }

        return router;
    }
}
