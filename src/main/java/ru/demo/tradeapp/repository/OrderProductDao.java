package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.OrderProduct;
public class OrderProductDao  extends BaseDao<OrderProduct> {
    public OrderProductDao() {
        super(OrderProduct.class);
    }
}
