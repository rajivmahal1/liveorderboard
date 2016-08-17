package com.rmahal.sbm.liveorderboard.domain;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.rmahal.sbm.liveorderboard.domain.OrderSummary.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.BUY;
import static com.rmahal.sbm.liveorderboard.utils.BigDecimalUtils.asBigDecimal;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ZohhakRunner.class)
public class OrderSummaryTest {

    @Test
    public void createsOrderSummary() {

        OrderSummary orderSummary = from(asBigDecimal("1.1"), asBigDecimal("2.2"), BUY, "userId");

        assertThat(orderSummary.getQuantity(), is(new BigDecimal("1.1")));
        assertThat(orderSummary.getPricePerQty(), is(new BigDecimal("2.2")));
        assertThat(orderSummary.getOrderType(), is(BUY));
        assertThat(orderSummary.getBreakdown(), is("userId"));
    }

    @Test
    public void createsOrderSummary_fromOrder() {

        Order order = Order.from("userId", asBigDecimal("1.1"), asBigDecimal("2.2"), BUY);

        OrderSummary orderSummary = OrderSummary.from(order);

        assertThat(orderSummary.getQuantity(), is(new BigDecimal("1.1")));
        assertThat(orderSummary.getPricePerQty(), is(new BigDecimal("2.2")));
        assertThat(orderSummary.getOrderType(), is(BUY));
        assertThat(orderSummary.getBreakdown(), is("userId 1.1"));
    }

    @Test
    public void mergesOrderSummary_fromOrder() {

        OrderSummary mergedOrderSummary = OrderSummary.merge(
                OrderSummary.from(Order.from("userId", asBigDecimal("1.0"), asBigDecimal("2.2"), BUY)),
                OrderSummary.from(Order.from("userId2", asBigDecimal("1.1"), asBigDecimal("2.2"), BUY))
        );

        assertThat(mergedOrderSummary.getQuantity(), is(new BigDecimal("2.1")));
        assertThat(mergedOrderSummary.getPricePerQty(), is(new BigDecimal("2.2")));
        assertThat(mergedOrderSummary.getOrderType(), is(BUY));
        assertThat(mergedOrderSummary.getBreakdown(), is("userId2 1.1, userId 1.0"));
    }

    @TestWith({
            "5, 5, 6, 6, BUY, BUY, user1 5, user1 5, true",
            "5, 0, 6, 6, BUY, BUY, user1 5, user1 5, false",
            "5, 5, 6, 0, BUY, BUY, user1 5, user1 5, false",
            "5, 5, 6, 6, BUY, SELL, user1 5, user1 5, false",
            "5, 5, 6, 6, BUY, BUY, user1 5, user2 5, false",
    })
    public void validatesHashcodeAndEquals(BigDecimal orderSummary1Quantity, BigDecimal orderSummary2Quantity,
                                           BigDecimal orderSummary1PricePerQty, BigDecimal orderSummary2PricePerQty,
                                           OrderType orderSummary1OrderSummaryType, OrderType orderSummary2OrderSummaryType,
                                           String orderSummary1Breakdown, String orderSummary2Breakdown,
                                           boolean expected) {

        OrderSummary orderSummary1 = new OrderSummary(orderSummary1Quantity, orderSummary1PricePerQty, orderSummary1OrderSummaryType, orderSummary1Breakdown);
        OrderSummary orderSummary2 = new OrderSummary(orderSummary2Quantity, orderSummary2PricePerQty, orderSummary2OrderSummaryType, orderSummary2Breakdown);

        assertThat(orderSummary1.equals(orderSummary2), is(expected));
        assertThat(orderSummary1.hashCode() == orderSummary2.hashCode(), is(expected));
    }

}