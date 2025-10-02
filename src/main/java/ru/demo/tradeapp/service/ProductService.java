package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.Product;
import ru.demo.tradeapp.repository.ProductDao;

import java.util.List;

public class ProductService {
    private ProductDao productDao = new ProductDao();

    public ProductService() {
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findOne(final long id) {
        return productDao.findOne(id);
    }

    public void save(final Product entity)
    {
        if (entity == null)
            return;
        productDao.save(entity);
    }

    public void update(final Product entity)
    {
        if (entity == null)
            return;
        productDao.update(entity);
    }

    public void delete(final Product entity)
    {
        if (entity == null)
            return;
        productDao.delete(entity);
    }

    public void deleteById(final Long id)
    {
        if (id == null)
            return;
        productDao.deleteById(id);
    }
}