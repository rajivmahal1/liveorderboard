package com.rmahal.sbm.liveorderboard.mappers;

import com.rmahal.sbm.liveorderboard.domain.Order;
import com.rmahal.sbm.liveorderboard.domain.OrderSummary;
import com.rmahal.sbm.liveorderboard.domain.OrderType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.rmahal.sbm.liveorderboard.domain.OrderSummary.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderSummary.merge;
import static java.math.BigDecimal.ZERO;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class OrderSummariesMapper {

    public List<OrderSummary> mergedForPricePerQty(List<Order> orders, OrderType orderType) {

        Map<BigDecimal, List<Order>> ordersForPricePerQty = orders.stream()
                .filter(order -> order.getOrderType().equals(orderType))
                .collect(groupingBy(Order::getPricePerQty));

        return ordersForPricePerQty.values().stream()
                .map(orderList -> orderList.stream()
                        .map(order -> from(order))
                        .reduce(from(ZERO, ZERO, orderType, ""), (a, b) -> merge(a, b))
                )
                .sorted(orderType.orderSummaryComparator())
                .collect(toList());
    }

}
