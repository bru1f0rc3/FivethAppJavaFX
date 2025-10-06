package ru.demo.tradeapp.util;

import ru.demo.tradeapp.model.Product;

public class Item {

    public Product getProduct() {
        return product;
    }

    public Integer getCount() {
        return count;
    }

    public Double getCost() {
        return cost;
    }

    public Double getTotal() {
        return total;
    }

    Product product;
    Integer count;
    Double cost;
    Double total;

    Item(Product product, Integer count, Double cost, Double total) {
        this.product = product;
        this.count = count;
        this.cost = cost;
        this.total = total;
    }
}