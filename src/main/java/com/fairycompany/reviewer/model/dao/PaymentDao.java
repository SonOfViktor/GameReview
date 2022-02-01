package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * The interface provides methods to control data in database table related with payments.
 */
public interface PaymentDao {
    /**
     * Find part of all payments of specified user in accordance with pagination properties.
     *
     * @param userId user id
     * @param skippedPayments the skipped rows of database table
     * @param rowAmount the row amount of database table to get
     * @return list of found payments
     * @throws DaoException if SQL exception occurred
     */
    List<Payment> findAllUserPayments(long userId, long skippedPayments, int rowAmount) throws DaoException;

    /**
     * Find total amount payments of specified user.
     *
     * @param userId user id
     * @return total amount of payments
     * @throws DaoException if SQL exception occurred
     */
    long findTotalPaymentAmount(long userId) throws DaoException;

    /**
     * Add user's payment to database.
     *
     * @param userId user id
     * @return id of added payment
     * @throws DaoException if SQL exception occurred
     */
    long addPayment(long userId) throws DaoException;

    /**
     * Add orders of payment to database.
     *
     * @param paymentId payment id
     * @param orders    orders
     * @throws DaoException if SQL exception occurred
     */
    void addOrders(long paymentId, Map<Order, Game> orders) throws DaoException;

    /**
     * Update balance of the shop.
     *
     * @param totalPrice total price
     * @return true if balance was updated
     * @throws DaoException if SQL exception occurred
     */
    boolean updateTotalBalance(BigDecimal totalPrice) throws DaoException;
}
