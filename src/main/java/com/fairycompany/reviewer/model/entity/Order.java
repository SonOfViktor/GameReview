package com.fairycompany.reviewer.model.entity;

import java.math.BigDecimal;

public class Order extends Entity {
    private long orderId;
    private long paymentId;
    private String gameName;
    private Platform platform;
    private BigDecimal price;
    private String gameKey;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getGameKey() {
        return gameKey;
    }

    public void setGameKey(String gameKey) {
        this.gameKey = gameKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (orderId != order.orderId) return false;
        if (paymentId != order.paymentId) return false;
        if (gameName != null ? !gameName.equals(order.gameName) : order.gameName != null) return false;
        if (platform != order.platform) return false;
        if (price != null ? !price.equals(order.price) : order.price != null) return false;
        return gameKey != null ? gameKey.equals(order.gameKey) : order.gameKey == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (orderId ^ (orderId >>> 32));
        result = 31 * result + (int) (paymentId ^ (paymentId >>> 32));
        result = 31 * result + (gameName != null ? gameName.hashCode() : 0);
        result = 31 * result + (platform != null ? platform.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (gameKey != null ? gameKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("Order{ ")
                .append("orderId = ").append(orderId)
                .append(", paymentId = ").append(paymentId)
                .append(", gameName = ").append(gameName)
                .append(", platform = ").append(platform)
                .append(", price = ").append(price)
                .append(", platforms = ").append(platform)
                .append(", gameKey = ").append(gameKey)
                .append(" }");
        return builder.toString();
    }

    public static class OrderBuilder {

        private Order order;

        public OrderBuilder() {
            order = new Order();
        }

        public OrderBuilder setOrderId(long orderId) {
            order.setOrderId(orderId);
            return this;
        }

        public OrderBuilder setGameName(String gameName) {
            order.setGameName(gameName);
            return this;
        }

        public OrderBuilder setPaymentId(long paymentId) {
            order.setPaymentId(paymentId);
            return this;
        }

        public OrderBuilder setPlatform(Platform platform) {
            order.setPlatform(platform);
            return this;
        }

        public OrderBuilder setPrice(BigDecimal price) {
            order.setPrice(price);
            return this;
        }

        public OrderBuilder setGameKey(String gameKey) {
            order.setGameKey(gameKey);
            return this;
        }

        public Order createOrder() {
            return order;
        }
    }
}
