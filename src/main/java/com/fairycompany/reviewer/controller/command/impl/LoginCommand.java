package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.fairycompany.reviewer.controller.command.RequestParameter.LOGIN;
import static com.fairycompany.reviewer.controller.command.RequestParameter.PASSWORD;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        HttpSession session = request.getSession();

        Router router = new Router(PagePath.MAIN_PAGE_REDIRECT);
        router.setType(Router.RouterType.REDIRECT);

        SessionRequestContent content = new SessionRequestContent();
        content.extractValues(request);


        UserService service = UserServiceImpl.getInstance();
        try {
            Optional<User> user = service.authenticate(content);
            if (user.isPresent()) {
                session.setAttribute(SessionAttribute.USER, user.get());
            } else {
                session.setAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.LOGIN_ERROR);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some Error when authentication ", e);
            throw new CommandException("Some error when authentication", e);
        }

        return router;
    }
}
