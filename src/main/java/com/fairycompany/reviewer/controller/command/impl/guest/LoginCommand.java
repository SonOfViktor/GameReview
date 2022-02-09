package com.fairycompany.reviewer.controller.command.impl.guest;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
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
            Optional<User> optionalUser = service.authenticateUser(content);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                checkUserStatus(user);
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

    private void checkUserStatus(User user) throws CommandException {
        switch(user.getUserStatus()) {
            case ACTIVE -> {
                content.addSessionAttribute(SessionAttribute.USER, user);
                Map<Order, Game> shoppingCart = new LinkedHashMap<>();
                content.addSessionAttribute(SessionAttribute.SHOPPING_CART, shoppingCart);
            }
            case NOT_CONFIRMED -> content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.USER_NOT_CONFIRMED);
            case BANNED -> content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.USER_BANNED);
            default -> {
                logger.log(Level.ERROR, "Unknown user status {}", user.getUserStatus());
                throw new CommandException("Unknown user status " + user.getUserStatus());
            }
        }
    }
}
