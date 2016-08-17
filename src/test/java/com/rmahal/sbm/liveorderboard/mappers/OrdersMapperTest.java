package com.rmahal.sbm.liveorderboard.mappers;

import com.rmahal.sbm.liveorderboard.domain.Order;
import com.rmahal.sbm.liveorderboard.domain.OrderSummary;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.rmahal.sbm.liveorderboard.domain.Order.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.BUY;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.SELL;
import static com.rmahal.sbm.liveorderboard.utils.BigDecimalUtils.asBigDecimal;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class OrdersMapperTest {

    private OrderSummariesMapper orderSummariesMapper = new OrderSummariesMapper();

    @Test
    public void mapsOrders_forBuys() {
        Order orderBuy1 = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY);
        Order orderBuy2 = from("userId", asBigDecimal("1.3"), asBigDecimal("2.3"), BUY);
        Order orderSell = from("userId", asBigDecimal("1.2"), asBigDecimal("2.2"), SELL);
        List<Order> orders = Arrays.asList(orderBuy1, orderBuy2, orderSell);

        List<OrderSummary> orderSummaries = orderSummariesMapper.mergedForPricePerQty(orders, BUY);

        assertThat(orderSummaries, contains(
                OrderSummary.from(asBigDecimal("1.3"), asBigDecimal("2.3"), BUY, "userId 1.3"),
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
    }

    @Test
    public void mapsOrders_forSells() {
        Order orderSell1 = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), SELL);
        Order orderSell2 = from("userId", asBigDecimal("1.3"), asBigDecimal("2.3"), SELL);
        Order orderBuy = from("userId", asBigDecimal("1.2"), asBigDecimal("2.2"), BUY);
        List<Order> orders = Arrays.asList(orderSell1, orderSell2, orderBuy);

        List<OrderSummary> orderSummaries = orderSummariesMapper.mergedForPricePerQty(orders, SELL);

        assertThat(orderSummaries, contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), SELL, "userId 1.1"),
                OrderSummary.from(asBigDecimal("1.3"), asBigDecimal("2.3"), SELL, "userId 1.3")
        ));
    }

    @Test
    public void mapsOrders_forMergedBuys() {
        Order orderBuy1 = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY);
        Order orderBuy2 = from("userId", asBigDecimal("1.3"), asBigDecimal("2.3"), BUY);
        Order orderBuy3 = from("userId2", asBigDecimal("1.7"), asBigDecimal("2.3"), BUY);
        List<Order> orders = Arrays.asList(orderBuy1, orderBuy2, orderBuy3);

        List<OrderSummary> orderSummaries = orderSummariesMapper.mergedForPricePerQty(orders, BUY);


        assertThat(orderSummaries, contains(
                OrderSummary.from(asBigDecimal("3.0"), asBigDecimal("2.3"), BUY, "userId2 1.7, userId 1.3"),
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
    }
    
    @Test
    public void mapsOrders_forMergedSells() {
        Order orderBuy1 = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), SELL);
        Order orderBuy2 = from("userId", asBigDecimal("1.3"), asBigDecimal("2.3"), SELL);
        Order orderBuy3 = from("userId2", asBigDecimal("1.7"), asBigDecimal("2.3"), SELL);
        List<Order> orders = Arrays.asList(orderBuy1, orderBuy2, orderBuy3);

        List<OrderSummary> orderSummaries = orderSummariesMapper.mergedForPricePerQty(orders, SELL);


        assertThat(orderSummaries, contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), SELL, "userId 1.1"),
                OrderSummary.from(asBigDecimal("3.0"), asBigDecimal("2.3"), SELL, "userId2 1.7, userId 1.3")
        ));
    }
}