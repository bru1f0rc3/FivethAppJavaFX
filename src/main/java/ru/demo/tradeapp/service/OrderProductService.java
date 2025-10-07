package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.OrderProduct;
import ru.demo.tradeapp.repository.OrderProductDao;

import java.util.List;

public class OrderProductService {
    private OrderProductDao orderProductDao = new OrderProductDao();

    public OrderProductService() {
    }

    public List<OrderProduct> findAll() {
        return orderProductDao.findAll();
    }

    public OrderProduct findOne(final long id) {
        return orderProductDao.findOne(id);
    }

    public void save(final OrderProduct entity) {
        if (entity == null)
            return;
        orderProductDao.save(entity);
    }

    public void update(final OrderProduct entity) {
        if (entity == null)
            return;
        orderProductDao.update(entity);
    }

    public void delete(final OrderProduct entity) {
        if (entity == null)
            return;
        orderProductDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        orderProductDao.deleteById(id);
    }

    public int getCount(String productId) {
        if (productId == null)
            return 0;
        return orderProductDao.getCount(productId);
    }
}
