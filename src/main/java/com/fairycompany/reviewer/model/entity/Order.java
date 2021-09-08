package com.fairycompany.reviewer.model.entity;

import java.time.LocalDateTime;

public class Order extends Entity {
    private long orderId;
    private long userId;
    private long gameId;
    private Platform platform;
    private LocalDateTime paymentDate;

    public Order(long orderId, long userId, long gameId, Platform platform, LocalDateTime paymentDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.gameId = gameId;
        this.platform = platform;
        this.paymentDate = paymentDate;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public LocalDateTime getPayment_date() {
        return paymentDate;
    }

    public void setPayment_date(LocalDateTime payment_date) {
        this.paymentDate = payment_date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (userId != order.userId) return false;
        if (gameId != order.gameId) return false;
        if (platform != order.platform) return false;
        return paymentDate != null ? paymentDate.equals(order.paymentDate) : order.paymentDate == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (gameId ^ (gameId >>> 32));
        result = 31 * result + (platform != null ? platform.hashCode() : 0);
        result = 31 * result + (paymentDate != null ? paymentDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("Order { ")
                .append("orderId = ").append(orderId)
                .append(", userId = ").append(userId)
                .append(", gameId = ").append(gameId)
                .append(", platform = ").append(platform)
                .append(", payment_date = ").append(paymentDate)
                .append(" }");

        return builder.toString();
    }
}
