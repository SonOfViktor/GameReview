package com.fairycompany.reviewer.model.dao.impl;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.dao.JdbcTemplate;
import com.fairycompany.reviewer.model.dao.PaymentDao;
import com.fairycompany.reviewer.model.dao.mapper.impl.OrderResultSetHandler;
import com.fairycompany.reviewer.model.dao.mapper.impl.PaymentResultSetHandler;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Payment;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

import static com.fairycompany.reviewer.model.dao.ColumnName.TOTAL_VALUE;

public class PaymentDaoImpl implements PaymentDao {
    private static final Logger logger = LogManager.getLogger();
    private static PaymentDaoImpl instance = new PaymentDaoImpl();

    private static final String FIND_ALL_USER_PAYMENTS_SQL = """
            SELECT payment_id, user_id, payment_date
            FROM payments
            WHERE user_id = ?
            ORDER BY payment_date LIMIT ?, ?
            """;

    private static final String FIND_ORDERS_WITH_PAYMENT_ID_SQL = """
            SELECT order_id, payment_id, name, orders.platform, game_key, order_price FROM game_rating.orders
            JOIN games ON games.game_id = orders.game_id
            WHERE payment_id = ?
            """;

    private static final String FIND_TOTAL_PAYMENT_AMOUNT = """
            SELECT COUNT(*) AS total_value FROM payments
            WHERE user_id = ?
            """;

    private static final String ADD_NEW_PAYMENT_SQL = """
            INSERT INTO payments (user_id)
            VALUES (?)
            """;

    private static final String ADD_ORDERS_SQL = """
            INSERT INTO orders (game_id, payment_id, platform, game_key, order_price)
            VALUE (?, ?, ?, ?, ?)
            """;

    private static final String UPDATE_TOTAL_BALANCE_SQL = """
            UPDATE shop_balance
            SET total_balance = (SELECT total_balance WHERE total_balance_id = 1) + ?
            WHERE total_balance_id = 1
            """;

    private JdbcTemplate<Payment> paymentJdbcTemplate;
    private JdbcTemplate<Order> orderJdbcTemplate;

    private PaymentDaoImpl() {
        paymentJdbcTemplate = new JdbcTemplate<>(new PaymentResultSetHandler());
        orderJdbcTemplate = new JdbcTemplate<>(new OrderResultSetHandler());
    }

    public static PaymentDaoImpl getInstance() {
        return instance;
    }

    public List<Payment> findAllUserPayments(long userId, long skippedPayments, int rowAmount) throws DaoException {
        List<Payment> payments = paymentJdbcTemplate.selectEntities(FIND_ALL_USER_PAYMENTS_SQL, userId, skippedPayments, rowAmount);

        for(Payment payment : payments) {
            payment.setOrders(findOrdersWithPaymentId(payment.getPaymentId()));
        }

        return payments;
    }

    public long findTotalPaymentAmount(long userId) throws DaoException {
        Number totalPaymentAmount = paymentJdbcTemplate.selectFieldsCalculation(FIND_TOTAL_PAYMENT_AMOUNT, TOTAL_VALUE, userId);
        logger.log(Level.DEBUG, "Total payment amount is {}", totalPaymentAmount);

        return (totalPaymentAmount != null) ? totalPaymentAmount.longValue() : 0;
    }

    @Override
    public long addPayment(long userId) throws DaoException {
        long paymentId = paymentJdbcTemplate.insertDataInTable(ADD_NEW_PAYMENT_SQL,
                userId);

        return paymentId;
    }

    @Override
    public void addOrders(long paymentId, Map<Order, Game> orders) throws DaoException {
        List<Object[]> batchArguments = new ArrayList<>();
        orders.forEach((key, value) -> {
            Object[] arguments = new Object[]{
                    value.getGameId(),
                    paymentId,
                    key.getPlatform().toString().toLowerCase(),
                    key.getGameKey(),
                    value.getPrice()
            };
            batchArguments.add(arguments);
        });

        orderJdbcTemplate.executeBatch(ADD_ORDERS_SQL, batchArguments);
    }

    @Override
    public boolean updateTotalBalance(BigDecimal totalPrice) throws DaoException {
        boolean isUpdated = paymentJdbcTemplate.updateDeleteFields(UPDATE_TOTAL_BALANCE_SQL, totalPrice);

        return isUpdated;
    }

    private List<Order> findOrdersWithPaymentId (long paymentId) throws DaoException {
        List<Order> orders = orderJdbcTemplate.selectEntities(FIND_ORDERS_WITH_PAYMENT_ID_SQL, paymentId);

        return orders;
    }
}
