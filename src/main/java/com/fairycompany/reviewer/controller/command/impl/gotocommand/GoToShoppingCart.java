package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

public class GoToShoppingCart implements Command {

    @Override
    public Router execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(SessionAttribute.CURRENT_PAGE, PagePath.SHOPPING_CART_REDIRECT);
        Map<Order, Game> shoppingCart = (Map<Order, Game>) session.getAttribute(SessionAttribute.SHOPPING_CART);

        BigDecimal totalPrice = shoppingCart.values().stream()
                .map(Game::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.setAttribute(RequestAttribute.TOTAL_PRICE, totalPrice);

        Router router = new Router(PagePath.SHOPPING_CART_PAGE);

        return router;
    }
}
