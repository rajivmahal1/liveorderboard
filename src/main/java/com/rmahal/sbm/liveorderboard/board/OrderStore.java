package com.rmahal.sbm.liveorderboard.board;

import com.rmahal.sbm.liveorderboard.domain.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrderStore {

    private List<Order> orders = new ArrayList<>();

    public void add(Order order) {
        orders.add(order);
    }

    public Optional<Order> get(Order order) {
        return orders.stream()
                .filter(o -> o.equals(order) )
                .findFirst();
    }

    public boolean remove(Order order) {
        return orders.remove(order);
    }

    public List<Order> all() {
        return Collections.unmodifiableList(orders);
    }
}
