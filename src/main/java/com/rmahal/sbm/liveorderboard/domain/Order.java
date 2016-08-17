package com.rmahal.sbm.liveorderboard.domain;

import java.math.BigDecimal;

public class Order {
    private final String userId;
    private final BigDecimal quantity;
    private final BigDecimal pricePerQty;
    private final OrderType orderType;

    public Order(String userId, BigDecimal quantity, BigDecimal pricePerQty, OrderType orderType) {
        this.userId = userId;
        this.quantity = quantity;
        this.pricePerQty = pricePerQty;
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPricePerQty() {
        return pricePerQty;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (userId != null ? !userId.equals(order.userId) : order.userId != null) return false;
        if (quantity != null ? !quantity.equals(order.quantity) : order.quantity != null) return false;
        if (pricePerQty != null ? !pricePerQty.equals(order.pricePerQty) : order.pricePerQty != null) return false;
        return orderType == order.orderType;

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (pricePerQty != null ? pricePerQty.hashCode() : 0);
        result = 31 * result + (orderType != null ? orderType.hashCode() : 0);
        return result;
    }

    public static Order from(String userId, BigDecimal quantity, BigDecimal pricePerQty, OrderType orderType) {
        return new Order(userId, quantity, pricePerQty, orderType);
    }

}
