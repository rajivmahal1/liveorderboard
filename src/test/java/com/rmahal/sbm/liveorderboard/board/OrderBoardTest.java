package com.rmahal.sbm.liveorderboard.board;

import com.rmahal.sbm.liveorderboard.domain.Order;
import com.rmahal.sbm.liveorderboard.domain.OrderSummary;
import com.rmahal.sbm.liveorderboard.mappers.OrderSummariesMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static com.rmahal.sbm.liveorderboard.domain.Order.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.BUY;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.SELL;
import static com.rmahal.sbm.liveorderboard.utils.BigDecimalUtils.asBigDecimal;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OrderBoardTest {

    @Mock
    private OrderStore orderStore;

    @Mock
    private OrderSummariesMapper orderSummariesMapper;

    private OrderBoard orderBoard;

    @Before
    public void before() {
        initMocks(this);
        orderBoard = new OrderBoard(orderStore, orderSummariesMapper);
    }

    @Test
    public void registersOrder() {
        Order order = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY);
        orderBoard.register(order);

        verify(orderStore).add(order);
    }

    @Test
    public void cancelsOrder() {

        Order order = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY);

        when(orderStore.remove(order)).thenReturn(true);

        assertThat(orderBoard.cancel(order), is(true));
        verify(orderStore).remove(order);
    }

    @Test
    public void doesNotCancel_forUnknownOrder() {

        Order order = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY);

        when(orderStore.remove(order)).thenReturn(false);

        assertThat(orderBoard.cancel(order), is(false));
        verify(orderStore).remove(order);
    }

    @Test
    public void getsOrderSummaries() {
        Order orderBuy1 = from("userId", asBigDecimal("1.1"), asBigDecimal("2.1"), BUY);
        Order orderBuy2 = from("userId", asBigDecimal("1.3"), asBigDecimal("2.3"), BUY);
        Order orderSell = from("userId", asBigDecimal("1.2"), asBigDecimal("2.2"), SELL);
        List<Order> orders = Arrays.asList(orderBuy1, orderBuy2, orderSell);

        when(orderStore.all()).thenReturn(orders);

        OrderSummary orderSummary1 = OrderSummary.from(asBigDecimal("1.3"), asBigDecimal("2.3"), BUY, "userId 1.3");
        OrderSummary orderSummary2 = OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1");
        List<OrderSummary> mappedOrderSummaries = Arrays.asList(orderSummary1, orderSummary2);

        when(orderSummariesMapper.mergedForPricePerQty(orders, BUY)).thenReturn(mappedOrderSummaries);

        List<OrderSummary> orderSummaries = orderBoard.summaries(BUY);

        assertThat(orderSummaries, contains(
            OrderSummary.from(asBigDecimal("1.3"), asBigDecimal("2.3"), BUY, "userId 1.3"),
            OrderSummary.from(asBigDecimal("1.1"), asBigDecimal("2.1"), BUY, "userId 1.1")
        ));
        verify(orderStore).all();
        verify(orderSummariesMapper).mergedForPricePerQty(orders, BUY);
    }

}
