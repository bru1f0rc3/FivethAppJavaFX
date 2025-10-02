package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.Product;

public class ProductDao extends BaseDao<Product> {
    public ProductDao() {
        super(Product.class);
    }
}
