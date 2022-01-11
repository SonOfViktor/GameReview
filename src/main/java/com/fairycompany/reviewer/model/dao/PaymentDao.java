package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.entity.Game;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PaymentDao {
    List<Payment> findAllUserPayments(long userId, long skippedUsers, int rowAmount) throws DaoException;

    long findTotalPaymentAmount() throws DaoException;

    long addPayment(long userId) throws DaoException;

    void addOrders(long paymentId, Map<Order, Game> orders) throws DaoException;

    boolean updateTotalBalance(BigDecimal totalPrice) throws DaoException;
}
