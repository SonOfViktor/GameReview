package com.fairycompany.reviewer.model.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Payment extends Entity {
    private long paymentId;
    private long userId;
    private LocalDateTime paymentDate;
    private List<Order> orders;

    public Payment(long paymentId, long userId, LocalDateTime paymentDate, List<Order> orders) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.paymentDate = paymentDate;
        this.orders = orders;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        if (paymentId != payment.paymentId) return false;
        if (userId != payment.userId) return false;
        if (paymentDate != null ? !paymentDate.equals(payment.paymentDate) : payment.paymentDate != null) return false;
        return orders != null ? orders.equals(payment.orders) : payment.orders == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (paymentId ^ (paymentId >>> 32));
        result = 31 * result + (int) (userId ^ (userId >>> 32));
        result = 31 * result + (paymentDate != null ? paymentDate.hashCode() : 0);
        result = 31 * result + (orders != null ? orders.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("Payment { ")
                .append("paymentId = ").append(paymentId)
                .append(", userId = ").append(userId)
                .append(", paymentDate = ").append(paymentDate)
                .append(", orders = ").append(orders)
                .append(" }");

        return builder.toString();
    }
}
