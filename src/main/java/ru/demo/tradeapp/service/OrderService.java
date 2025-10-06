package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.Order;
import ru.demo.tradeapp.repository.OrderDao;

import java.util.List;

public class OrderService {
    private OrderDao orderDao = new OrderDao();

    public OrderService() {
    }

    public List<Order> findAll() {
        return orderDao.findAll();
    }

    public Order findOne(final long id) {
        return orderDao.findOne(id);
    }

    public void save(final Order entity) {
        if (entity == null)
            return;
        orderDao.save(entity);
    }

    public void update(final Order entity) {
        if (entity == null)
            return;
        orderDao.update(entity);
    }

    public void delete(final Order entity) {
        if (entity == null)
            return;
        orderDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        orderDao.deleteById(id);
    }
}

