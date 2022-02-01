package com.fairycompany.reviewer.controller.command.impl.user;

import com.fairycompany.reviewer.controller.command.Command;
import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class LogoutCommand implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        Router router = new Router(PagePath.MAIN_PAGE_REDIRECT);
        router.setType(Router.RouterType.REDIRECT);

        HttpSession session = request.getSession();
        session.removeAttribute(SessionAttribute.USER);
        session.invalidate();

        return router;
    }
}
