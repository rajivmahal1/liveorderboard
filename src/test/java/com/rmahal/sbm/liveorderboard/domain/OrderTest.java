package com.rmahal.sbm.liveorderboard.domain;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static com.rmahal.sbm.liveorderboard.domain.Order.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.BUY;
import static com.rmahal.sbm.liveorderboard.utils.BigDecimalUtils.asBigDecimal;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(ZohhakRunner.class)
public class OrderTest {

    @Test
    public void createsOrder() {

        Order order = from("userId", asBigDecimal("1.1"), asBigDecimal("2.2"), BUY);

        assertThat(order.getUserId(), is("userId"));
        assertThat(order.getQuantity(), is(new BigDecimal("1.1")));
        assertThat(order.getPricePerQty(), is(new BigDecimal("2.2")));
        assertThat(order.getOrderType(), is(BUY));
    }

    @TestWith({
            "user1, user1, 5, 5, 6, 6, BUY, BUY, true",
            "user1, user2, 5, 5, 6, 6, BUY, BUY, false",
            "user1, user1, 5, 0, 6, 6, BUY, BUY, false",
            "user1, user1, 5, 5, 6, 0, BUY, BUY, false",
            "user1, user1, 5, 5, 6, 6, BUY, SELL, false",
    })
    public void validatesHashcodeAndEquals(String order1UserId, String order2UserId,
                                           BigDecimal order1Quantity, BigDecimal order2Quantity,
                                           BigDecimal order1PricePerQty, BigDecimal order2PricePerQty,
                                           OrderType order1OrderType, OrderType order2OrderType,
                                           boolean expected) {

        Order order1 = new Order(order1UserId, order1Quantity, order1PricePerQty, order1OrderType);
        Order order2 = new Order(order2UserId, order2Quantity, order2PricePerQty, order2OrderType);

        assertThat(order1.equals(order2), is(expected));
        assertThat(order1.hashCode() == order2.hashCode(), is(expected));
    }
}