package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.Command;
import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

import static com.fairycompany.reviewer.controller.command.RequestParameter.LOGIN_PARAM;
import static com.fairycompany.reviewer.controller.command.RequestParameter.PASSWORD_PARAM;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router(request.getContextPath());
        router.setType(Router.RouterType.REDIRECT);

        HttpSession session = request.getSession();
        String login = request.getParameter(LOGIN_PARAM);
        String password = request.getParameter(PASSWORD_PARAM);

        UserServiceImpl service = new UserServiceImpl();
        try {
            Optional<User> user = service.authenticate(login, password);
            if (user.isPresent()) {
                session.setAttribute("user", user.get());
                router.setType(Router.RouterType.FORWARD);
                router.setPage(PagePath.MAIN_PAGE);
            } else {
                session.setAttribute("errorLoginPassMessage", "This login or password is wrong");
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some Error when authentication ", e);
            session.setAttribute("nullPage", "Some Error when authentication");
        }

        return router;
    }
}
