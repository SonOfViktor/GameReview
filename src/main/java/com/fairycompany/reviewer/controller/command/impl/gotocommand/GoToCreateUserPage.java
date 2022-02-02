package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.Command;
import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class GoToCreateUserPage implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.CURRENT_PAGE, PagePath.CREATE_USER_REDIRECT);

        Router router = new Router(PagePath.CREATE_USER);
        return router;
    }
}
