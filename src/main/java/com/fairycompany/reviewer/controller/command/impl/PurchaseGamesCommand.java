package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.Router;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.service.PaymentService;
import com.fairycompany.reviewer.model.service.impl.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

public class PurchaseGamesCommand extends AbstractCommand {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        initCommand(request);

        PaymentService paymentService = PaymentServiceImpl.getInstance();

        try {
            if (paymentService.addPayment(content)) {
                router.setPage(PagePath.MAIN_PAGE_REDIRECT);
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE, LocaleMessageKey.PURCHASE_GAME_SUCCESS);
            } else {
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.PURCHASE_GAME_FAILED);
            }
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Purchase games failed. {}", e.getMessage());
            throw new CommandException("Purchase games failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
