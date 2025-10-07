package ru.demo.tradeapp.repository;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.demo.tradeapp.model.Order;
import ru.demo.tradeapp.model.OrderProduct;
import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.util.Item;

import java.util.ArrayList;
import java.util.List;

import static ru.demo.tradeapp.util.Manager.mainBasket;

public class OrderDao extends BaseDao<Order> {
    public OrderDao() {
        super(Order.class);
    }

    @Override
    public void save(Order entity) {
        Session session = getCurrentSession();
        Transaction tx1 = session.beginTransaction();
        List<OrderProduct> orderProducts = new ArrayList<>();
        for (Item item : mainBasket.getBasket().values()) {
            Product currentProduct = session.get(Product.class, item.getProduct().getProductId());
            currentProduct.setQuantityInStock(currentProduct.getQuantityInStock() - item.getCount());
            session.merge(currentProduct);
            OrderProduct orderProduct = new OrderProduct(entity, currentProduct, Long.valueOf(item.getCount()));
            orderProducts.add(orderProduct);
        }
        session.flush();
        entity.setOrderProducts(orderProducts);
        session.persist(entity);
        tx1.commit();
        session.close();
        mainBasket.getBasket().clear();
    }

}

