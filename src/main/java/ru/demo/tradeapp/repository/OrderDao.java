package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.Order;

public class OrderDao  extends BaseDao<Order> {
    public OrderDao() {
        super(Order.class);
    }
}
