package com.fairycompany.reviewer.model.dao.mapper.impl;

import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Payment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class PaymentResultSetHandler implements ResultSetHandler<Payment> {
    @Override
    public Payment resultSetToObject(ResultSet resultSet) throws SQLException {
        long paymentId = resultSet.getLong(PAYMENT_ID);
        long userId = resultSet.getLong(USER_ID);
        LocalDateTime paymentDate = resultSet.getTimestamp(PAYMENT_DATE).toLocalDateTime();
        List<Order> orders = new ArrayList<>();

        Payment payment = new Payment(paymentId, userId, paymentDate, orders);

        return payment;
    }
}
