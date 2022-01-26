package com.fairycompany.reviewer.controller.command.impl.gotocommand;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.controller.command.impl.AbstractCommand;
import com.fairycompany.reviewer.exception.CommandException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.entity.Payment;
import com.fairycompany.reviewer.model.service.PaymentService;
import com.fairycompany.reviewer.model.service.impl.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;

import java.util.List;

public class GoToPaymentPage extends AbstractCommand {

    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        String paymentRedirect = PagePath.PAYMENT_REDIRECT + request.getParameter(RequestParameter.ACTUAL_PAGE);

        content.extractValues(request);
        content.addSessionAttribute(SessionAttribute.CURRENT_PAGE, paymentRedirect);
        Router router = new Router(PagePath.PAYMENT_PAGE);

        PaymentService paymentService = PaymentServiceImpl.getInstance();

        try {
            List<Payment> payments = paymentService.findAllUserPayments(content);
            content.addRequestAttribute(RequestAttribute.PAYMENT_LIST, payments);
        } catch (ServiceException e) {
            logger.log(Level.ERROR, "Finding payments failed. {}", e.getMessage());
            throw new CommandException("Finding payments failed", e);
        }
        content.insertValues(request);

        return router;
    }
}
