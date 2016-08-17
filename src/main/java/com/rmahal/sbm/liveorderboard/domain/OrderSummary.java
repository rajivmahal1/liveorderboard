package com.rmahal.sbm.liveorderboard.domain;

import java.math.BigDecimal;

import static java.lang.String.format;

public class OrderSummary {
    private final BigDecimal quantity;
    private final BigDecimal pricePerQty;
    private final OrderType orderType;
    private final String breakdown;

    public OrderSummary(BigDecimal quantity, BigDecimal pricePerQty, OrderType orderType, String breakdown) {
        this.quantity = quantity;
        this.pricePerQty = pricePerQty;
        this.orderType = orderType;
        this.breakdown = breakdown;
    }

    public String getBreakdown() {
        return breakdown;
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

        OrderSummary that = (OrderSummary) o;

        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        if (pricePerQty != null ? !pricePerQty.equals(that.pricePerQty) : that.pricePerQty != null) return false;
        if (orderType != that.orderType) return false;
        return breakdown != null ? breakdown.equals(that.breakdown) : that.breakdown == null;
    }

    @Override
    public int hashCode() {
        int result = quantity != null ? quantity.hashCode() : 0;
        result = 31 * result + (pricePerQty != null ? pricePerQty.hashCode() : 0);
        result = 31 * result + (orderType != null ? orderType.hashCode() : 0);
        result = 31 * result + (breakdown != null ? breakdown.hashCode() : 0);
        return result;
    }

    public static OrderSummary from(BigDecimal quantity, BigDecimal pricePerQty, OrderType orderType, String breakdown) {
        return new OrderSummary(quantity, pricePerQty, orderType, breakdown);
    }

    public static OrderSummary from(Order order) {
        return OrderSummary.from(
                order.getQuantity(), order.getPricePerQty(), order.getOrderType(),
                format("%s %s", order.getUserId(), order.getQuantity()));
    }

    public static OrderSummary merge(OrderSummary orderSummary, OrderSummary otherSummary) {
        return from(
                orderSummary.getQuantity().add(otherSummary.getQuantity()),
                otherSummary.getPricePerQty(),
                otherSummary.getOrderType(),
                format("%s, %s", otherSummary.getBreakdown(), orderSummary.getBreakdown()).replaceFirst(", $",""));
    }

}
