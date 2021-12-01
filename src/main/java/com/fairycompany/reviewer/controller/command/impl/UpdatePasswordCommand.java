package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdatePasswordCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();
        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);

        String currentPage = (String) content.getSessionAttribute(SessionAttribute.CURRENT_PAGE);
        Router router = new Router(currentPage);
        router.setType(Router.RouterType.REDIRECT);

        UserService userService = UserServiceImpl.getInstance();

        try {
            if (userService.updatePassword(content)) {
                session.setAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.PASSWORD_UPDATED);
                logger.log(Level.DEBUG, "Password updated");
            } else {
                session.setAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.UPDATE_PASSWORD_FAILED);
            }
        } catch (ServiceException e) {
            e.printStackTrace();    //todo
        }
        return router;
    }
}
