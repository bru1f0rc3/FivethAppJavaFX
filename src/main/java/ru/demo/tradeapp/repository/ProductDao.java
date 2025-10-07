package ru.demo.tradeapp.repository;

import org.hibernate.Session;
import ru.demo.tradeapp.model.Product;

import java.util.List;
import java.util.Objects;

public class ProductDao extends BaseDao<Product> {
    public ProductDao() {
        super(Product.class);
    }
    public Product findOne(final String id) {
        return getCurrentSession().get(Product.class, id);
    }
    public List<Product> findAllWithDetails() {
        Session session = getCurrentSession();
        List<Product> items = session.createQuery(
                "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.manufacturer LEFT JOIN FETCH p.supplier LEFT JOIN FETCH p.category",
                Product.class).list();
        session.close();
        return items;
    }
}