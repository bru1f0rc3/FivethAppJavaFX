package ru.demo.tradeapp.model;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@IdClass(OrderProductId.class)
@Table(name = "order_product", schema = "public")
public class OrderProduct {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "count", nullable = false)
    private Long count;

    public OrderProduct(Order order, Product product, Long count) {
        this.order = order;
        this.product = product;
        this.count = count;
    }

    public OrderProduct(Product product, Long count) {
        this.product = product;
        this.count = count;
    }
    public OrderProduct() {
        this.product = product;
        this.count = count;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProduct that)) return false;
        return Objects.equals(order, that.order) && Objects.equals(product, that.product) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return 17 * order.getOrderId().hashCode() +
                31 * product.getProductId().hashCode() +
                17 * count.hashCode();
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
