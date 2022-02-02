package com.fairycompany.reviewer.model.dao.mapper.impl;

import com.fairycompany.reviewer.model.dao.mapper.ResultSetHandler;
import com.fairycompany.reviewer.model.entity.Order;
import com.fairycompany.reviewer.model.entity.Platform;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.fairycompany.reviewer.model.dao.ColumnName.*;

public class OrderResultSetHandler implements ResultSetHandler<Order> {

    @Override
    public Order resultSetToObject(ResultSet resultSet) throws SQLException {
        Order order = new Order.OrderBuilder()
                .setOrderId(resultSet.getLong(ORDER_ID))
                .setPaymentId(resultSet.getLong(PAYMENT_ID))
                .setGameName(resultSet.getString(NAME))
                .setPlatform(Platform.valueOf(resultSet.getString(PLATFORM).toUpperCase()))
                .setGameKey(resultSet.getString(ORDER_GAME_KEY))
                .setPrice(resultSet.getBigDecimal(ORDER_PRICE))
                .createOrder();
        return order;
    }
}
