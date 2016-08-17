package com.rmahal.sbm.liveorderboard.board;

import com.rmahal.sbm.liveorderboard.domain.Order;
import org.junit.Test;

import java.util.List;

import static com.rmahal.sbm.liveorderboard.domain.Order.from;
import static com.rmahal.sbm.liveorderboard.domain.OrderType.BUY;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class OrderStoreTest {

    private OrderStore orderStore = new OrderStore();

    @Test
    public void addsOrder() {
        Order order = from("user1", ONE, TEN, BUY);

        orderStore.add(order);

        assertThat(orderStore.get(order).get(), is(order));
    }

    @Test
    public void doesNotGetOrder() {
        Order order = from("user1", ONE, TEN, BUY);

        assertThat(orderStore.get(order).isPresent(), is(false));
    }

    @Test
    public void removesOrder() {
        Order order1 = from("user1", ONE, TEN, BUY);
        Order order2 = from("user2", ONE, TEN, BUY);

        orderStore.add(order1);
        orderStore.add(order2);

        assertThat(orderStore.remove(order2), is(true));

        assertThat(orderStore.get(order1).get(), is(order1));
        assertThat(orderStore.get(order2).isPresent(), is(false));
    }

    @Test
    public void doesNotRemove_forUnknownOrder() {
        Order order1 = from("user1", ONE, TEN, BUY);
        Order order2 = from("user2", ONE, TEN, BUY);

        orderStore.add(order1);

        assertThat(orderStore.remove(order2), is(false));

        assertThat(orderStore.get(order1).get(), is(order1));
        assertThat(orderStore.get(order2).isPresent(), is(false));
    }

    @Test
    public void getsAllOrders() {
        Order order1 = from("user1", ONE, TEN, BUY);
        Order order2 = from("user2", ONE, TEN, BUY);

        orderStore.add(order1);
        orderStore.add(order2);

        List<Order> orders = orderStore.all();

        assertThat(orders, containsInAnyOrder(order1, order2));
    }
}