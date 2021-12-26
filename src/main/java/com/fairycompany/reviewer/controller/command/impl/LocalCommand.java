package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.Command;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LocalCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String currentPage = (String) session.getAttribute(SessionAttribute.CURRENT_PAGE);
        logger.log(Level.DEBUG, "Current page is " + currentPage);

        Router router = new Router(currentPage);
//        router.setType(Router.RouterType.REDIRECT);       //todo

        String currentLocale = (String) session.getAttribute(SessionAttribute.SESSION_LOCALE);
        String newLocale = request.getParameter(RequestParameter.LOCALE);

        if (!newLocale.equals(currentLocale)) {
            session.setAttribute(SessionAttribute.SESSION_LOCALE, newLocale);
            logger.log(Level.DEBUG, "Language changes to {} ", newLocale);
        }

        return router;
    }
}
