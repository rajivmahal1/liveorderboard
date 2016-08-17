package com.rmahal.sbm.liveorderboard.board;

import com.rmahal.sbm.liveorderboard.domain.Order;
import com.rmahal.sbm.liveorderboard.domain.OrderSummary;
import com.rmahal.sbm.liveorderboard.domain.OrderType;
import com.rmahal.sbm.liveorderboard.mappers.OrderSummariesMapper;

import java.util.List;

public class OrderBoard {

    private final OrderStore orderStore;
    private final OrderSummariesMapper orderSummariesMapper;

    public OrderBoard(OrderStore orderStore, OrderSummariesMapper orderSummariesMapper) {
        this.orderStore = orderStore;
        this.orderSummariesMapper = orderSummariesMapper;
    }

    public void register(Order order) {
        orderStore.add(order);
    }

    public boolean cancel(Order order) {
        return orderStore.remove(order);
    }

    public List<OrderSummary> summaries(OrderType orderType) {
        List<Order> allOrders = orderStore.all();
        return orderSummariesMapper.mergedForPricePerQty(allOrders, orderType);
    }

}
