package com.rmahal.sbm.liveorderboard.domain;

import java.util.Comparator;

public enum OrderType {
    SELL(Comparator.comparingDouble(os -> os.getPricePerQty().doubleValue())),
    BUY(SELL.orderSummaryComparator.reversed());

    private final Comparator<OrderSummary> orderSummaryComparator;

    OrderType(Comparator<OrderSummary> orderSummaryComparator) {
        this.orderSummaryComparator = orderSummaryComparator;
    }

    public Comparator<OrderSummary> orderSummaryComparator() {
        return orderSummaryComparator;
    }
}
