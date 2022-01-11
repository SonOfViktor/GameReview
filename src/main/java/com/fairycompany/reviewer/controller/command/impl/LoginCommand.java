package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.UserService;
import com.fairycompany.reviewer.model.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.*;

public class LoginCommand extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        content.extractValues(request);

        Router router = new Router(PagePath.MAIN_PAGE_REDIRECT);
        router.setType(Router.RouterType.REDIRECT);

        UserService service = UserServiceImpl.getInstance();
        try {
            Optional<User> user = service.authenticate(content);
            if (user.isPresent()) {
                content.addSessionAttribute(SessionAttribute.USER, user.get());
                Map<Order, Game> shoppingCart = new LinkedHashMap<>();
                content.addSessionAttribute(SessionAttribute.SHOPPING_CART, shoppingCart);
            } else {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.LOGIN_ERROR);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Some Error when authentication ", e);
            throw new CommandException("Some error when authentication", e);
        }
        content.insertValues(request);

        return router;
    }
}
