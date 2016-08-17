package com.rmahal.sbm.liveorderboard.board;

import com.rmahal.sbm.liveorderboard.domain.OrderSummary;
import com.rmahal.sbm.liveorderboard.domain.OrderType;
import com.rmahal.sbm.liveorderboard.mappers.OrderSummariesMapper;
import org.junit.Test;

import java.util.Collections;

import static com.rmahal.sbm.liveorderboard.domain.Order.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.BUY;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.SELL;
import static com.rmahal.sbm.liveorderboard.utils.BigDecimalUtils.asBigDecimal;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderBoardAcceptanceTest {

    private OrderBoard orderBoard = new OrderBoard(new OrderStore(), new OrderSummariesMapper());

    @Test
    public void givenNoRegisteredOrders_thenNoBuyOrSellLiveOrders() {
        assertThat(orderBoard.summaries(OrderType.BUY), is(Collections.emptyList()));
        assertThat(orderBoard.summaries(SELL), is(Collections.emptyList()));
    }

    @Test
    public void givenARegisteredBuyOrder_thenOnlyABuyLiveOrder() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY));

        assertThat(orderBoard.summaries(SELL), is(Collections.emptyList()));
        assertThat(orderBoard.summaries(OrderType.BUY), contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
    }

    @Test
    public void givenRegisteredBuyAndSellOrders_thenBothABuyAndSellOrderAreLive() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));

        assertThat(orderBoard.summaries(OrderType.BUY), contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
        assertThat(orderBoard.summaries(OrderType.SELL), contains(
                OrderSummary.from(asBigDecimal("2.1"), asBigDecimal("3.1"), SELL, "userId 2.1")
        ));
    }

    @Test
    public void givenMultipleRegisteredBuyOrdersWithDifferingPricePerQty_thenBuyOrdersAreShownAsLiveInPricePerQtyAscending() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));

        assertThat(orderBoard.summaries(OrderType.BUY), contains(
                OrderSummary.from(asBigDecimal("2.1"), asBigDecimal("3.1"), BUY, "userId 2.1"),
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
    }

    @Test
    public void givenMultipleRegisteredSellOrdersWithDifferingPricePerQty_thenSellOrdersAreShownAsLiveInPricePerQtyDescending() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), SELL));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));

        assertThat(orderBoard.summaries(OrderType.SELL), contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), SELL, "userId 1.1"),
                OrderSummary.from(asBigDecimal("2.1"), asBigDecimal("3.1"), SELL, "userId 2.1")
        ));
    }

    @Test
    public void givenMultipleRegisteredBuyOrdersWithSamePricePerQty_thenSamePriceBuyOrdersAreShownAsMerged() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));
        orderBoard.register(from("userId2", asBigDecimal("2.9"), asBigDecimal("3.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));

        assertThat(orderBoard.summaries(OrderType.BUY), contains(
                OrderSummary.from(asBigDecimal("5.0"), asBigDecimal("3.1"), BUY, "userId2 2.9, userId 2.1"),
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
    }

    @Test
    public void givenMultipleRegisteredSellOrdersWithSamePricePerQty_thenSamePriceSellOrdersAreShownAsMerged() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), SELL));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));
        orderBoard.register(from("userId2", asBigDecimal("2.9"), asBigDecimal("3.1"), SELL));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));

        assertThat(orderBoard.summaries(OrderType.SELL), contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), SELL, "userId 1.1"),
                OrderSummary.from(asBigDecimal("5.0"), asBigDecimal("3.1"), SELL, "userId2 2.9, userId 2.1")
        ));
    }

    @Test
    public void givenCancelledBuyOrderWithSamePricePerQtyAsAnother_thenRemainingSamePriceBuyOrdersIsLive() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));
        orderBoard.register(from("userId2", asBigDecimal("2.9"), asBigDecimal("3.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));

        boolean cancelled = orderBoard.cancel(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));

        assertThat(cancelled, is(true));
        assertThat(orderBoard.summaries(OrderType.BUY), contains(
                OrderSummary.from(asBigDecimal("2.9"), asBigDecimal("3.1"), BUY, "userId2 2.9"),
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
        assertThat(orderBoard.summaries(OrderType.SELL), contains(
                OrderSummary.from(asBigDecimal("2.1"), asBigDecimal("3.1"), SELL, "userId 2.1")
        ));
    }
    
    @Test
    public void givenCancelledSellOrderWithSamePricePerQtyAsAnother_thenRemainingSamePriceSellOrdersIsLive() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), SELL));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));
        orderBoard.register(from("userId2", asBigDecimal("2.9"), asBigDecimal("3.1"), SELL));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));

        boolean cancelled = orderBoard.cancel(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));

        assertThat(cancelled, is(true));
        assertThat(orderBoard.summaries(OrderType.SELL), contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), SELL, "userId 1.1"),
                OrderSummary.from(asBigDecimal("2.9"), asBigDecimal("3.1"), SELL, "userId2 2.9")
        ));
        assertThat(orderBoard.summaries(OrderType.BUY), contains(
                OrderSummary.from(asBigDecimal("2.1"), asBigDecimal("3.1"), BUY, "userId 2.1")
        ));
    }

    @Test
    public void givenCancelOfNonExistentOrders_thenLiveOrdersIsUnchanged() {
        orderBoard.register(from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY));
        orderBoard.register(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), SELL));

        boolean cancelled = orderBoard.cancel(from("userId", asBigDecimal("2.1"), asBigDecimal("3.1"), BUY));

        assertThat(cancelled, is(false));
        assertThat(orderBoard.summaries(OrderType.BUY), contains(
                OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
        assertThat(orderBoard.summaries(OrderType.SELL), contains(
                OrderSummary.from(asBigDecimal("2.1"), asBigDecimal("3.1"), SELL, "userId 2.1")
        ));
    }

}
