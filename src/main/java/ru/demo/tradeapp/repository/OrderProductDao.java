package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.OrderProduct;

import java.util.Objects;

public class OrderProductDao  extends BaseDao<OrderProduct> {
    public OrderProductDao() {
        super(OrderProduct.class);
    }

    public int getCount(String productId)
    {
        int k = findAll().stream().filter(o -> Objects.equals(o.getProduct().getProductId(), productId)).toList().size();
        return k;
    }
}
