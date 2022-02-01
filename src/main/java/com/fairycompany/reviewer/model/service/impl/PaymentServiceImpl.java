package com.fairycompany.reviewer.model.service.impl;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.exception.ServiceException;
import com.fairycompany.reviewer.model.dao.PaymentDao;
import com.fairycompany.reviewer.model.dao.TransactionManager;
import com.fairycompany.reviewer.model.dao.UserDao;
import com.fairycompany.reviewer.model.dao.impl.PaymentDaoImpl;
import com.fairycompany.reviewer.model.dao.impl.UserDaoImpl;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Payment;
import com.fairycompany.reviewer.model.entity.User;
import com.fairycompany.reviewer.model.service.PaymentService;
import com.fairycompany.reviewer.model.util.ServiceUtil;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.fairycompany.reviewer.controller.command.RequestParameter.ROW_AMOUNT;

public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LogManager.getLogger();
    private PaymentDao paymentDao = PaymentDaoImpl.getInstance();
    private UserDao userDao = UserDaoImpl.getInstance();
    private TransactionManager transactionManager = TransactionManager.getInstance();
    private static PaymentServiceImpl instance = new PaymentServiceImpl();

    private PaymentServiceImpl() {
    }

    public static PaymentService getInstance(){
        return instance;
    }

    public List<Payment> findAllUserPayments(SessionRequestContent content) throws ServiceException {
        ServiceUtil serviceUtil = ServiceUtil.getInstance();

        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        long actualPage = serviceUtil.takeActualPage(content);
        int rowAmount = Integer.parseInt(content.getSessionAttribute(ROW_AMOUNT).toString());

        List<Payment> payments;

        try {
            transactionManager.initTransaction();

            long skippedPayments = actualPage * rowAmount - rowAmount;
            payments = paymentDao.findAllUserPayments(user.getUserId(), skippedPayments, rowAmount);

            List<BigDecimal> totalPrices = calculateTotalPrices(payments);

            long totalPaymentAmount = paymentDao.findTotalPaymentAmount(user.getUserId());
            long pageAmount = (long) Math.ceil((double) totalPaymentAmount / rowAmount);

            content.addRequestAttribute(RequestAttribute.TOTAL_PRICE_LIST, totalPrices);
            content.addRequestAttribute(RequestAttribute.PAGE_AMOUNT, pageAmount);
            content.addRequestAttribute(RequestAttribute.ACTUAL_PAGE, actualPage);
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when finding payments, {}", e.getMessage());
            throw new ServiceException("Error when finding payments", e);
        } finally {
            transactionManager.endTransaction();
        }

        return payments;
    }

    public boolean addPayment(SessionRequestContent content) throws ServiceException {
        boolean isPaymentAdded = false;
        User user = (User) content.getSessionAttribute(SessionAttribute.USER);
        Map<Order, Game> shoppingCart = (Map<Order, Game>) content.getSessionAttribute(SessionAttribute.SHOPPING_CART);

        try {
            transactionManager.initTransaction();

            BigDecimal userBalance = userDao.findUserBalance(user.getUserId());
            BigDecimal totalPrice = calculateTotalPrice(shoppingCart);

            if (userBalance.compareTo(totalPrice) >= 0) {
                userBalance = userBalance.subtract(totalPrice);
                userDao.updateUserBalance(user.getUserId(), userBalance);

                paymentDao.updateTotalBalance(totalPrice);
                long paymentId = paymentDao.addPayment(user.getUserId());
                generateGameKey(shoppingCart);
                paymentDao.addOrders(paymentId, shoppingCart);

                shoppingCart.clear();
                isPaymentAdded = true;
            }
        } catch (DaoException e) {
            transactionManager.rollback();
            logger.log(Level.ERROR, "Error when adding payment, {}", e.getMessage());
            throw new ServiceException("Error when adding payment", e);
        } finally {
            transactionManager.endTransaction();
        }

        return isPaymentAdded;
    }

    private BigDecimal calculateTotalPrice(Map<Order, Game> shoppingCart) {
        BigDecimal totalPrice = shoppingCart.values().stream()
                .map(Game::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalPrice;
    }

    private List<BigDecimal> calculateTotalPrices(List<Payment> payments) {
        List<BigDecimal> totalPrices = payments.stream()
                .map(payment -> payment.getOrders().stream()
                        .map(Order::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .toList();
        return totalPrices;
    }

    private void generateGameKey(Map<Order, Game> shoppingCart) {
        shoppingCart.keySet().forEach(order -> order.setGameKey(UUID.randomUUID().toString()));
    }
}
