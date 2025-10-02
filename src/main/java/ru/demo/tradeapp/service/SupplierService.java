package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.Supplier;
import ru.demo.tradeapp.repository.SupplierDao;

import java.util.List;

public class SupplierService {
    private SupplierDao supplierDao = new SupplierDao();

    public SupplierService() {
    }

    public List<Supplier> findAll() {
        return supplierDao.findAll();
    }

    public Supplier findOne(final long id) {
        return supplierDao.findOne(id);
    }

    public void save(final Supplier entity) {
        if (entity == null)
            return;
        supplierDao.save(entity);
    }

    public void update(final Supplier entity) {
        if (entity == null)
            return;
        supplierDao.update(entity);
    }

    public void delete(final Supplier entity) {
        if (entity == null)
            return;
        supplierDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        supplierDao.deleteById(id);
    }
}