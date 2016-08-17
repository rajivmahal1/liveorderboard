package com.rmahal.sbm.liveorderboard.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.rmahal.sbm.liveorderboard.domain.OrderSummary.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.BUY;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.SELL;
import static com.rmahal.sbm.liveorderboard.utils.BigDecimalUtils.asBigDecimal;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderTypeTest {

    @Test
    public void sorts_forSells() {
        OrderSummary orderSummary1 = from(asBigDecimal("1.1"), asBigDecimal("2.3"), SELL, "userId 1.1");
        OrderSummary orderSummary2 = from(asBigDecimal("1.1"), asBigDecimal("2.1"), SELL, "userId 1.1");

        List<OrderSummary> orderSummaries = Arrays.asList(orderSummary1, orderSummary2)
                .stream().sorted(SELL.orderSummaryComparator())
                .collect(Collectors.toList());

        assertThat(orderSummaries.size(), is(2));
        assertThat(orderSummaries.get(0), is(orderSummary2));
        assertThat(orderSummaries.get(1), is(orderSummary1));
    }

    @Test
    public void sorts_forBuys() {
        OrderSummary orderSummary1 = from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1");
        OrderSummary orderSummary2 = from(asBigDecimal("1.1"), asBigDecimal("2.3"), BUY, "userId 1.1");

        List<OrderSummary> orderSummaries = Arrays.asList(orderSummary1, orderSummary2)
                .stream().sorted(BUY.orderSummaryComparator())
                .collect(Collectors.toList());

        assertThat(orderSummaries.size(), is(2));
        assertThat(orderSummaries.get(0), is(orderSummary2));
        assertThat(orderSummaries.get(1), is(orderSummary1));
    }}