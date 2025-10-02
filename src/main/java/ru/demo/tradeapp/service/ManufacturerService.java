package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.Manufacturer;
import ru.demo.tradeapp.repository.ManufacturerDao;

import java.util.List;

public class ManufacturerService {
    private ManufacturerDao manufacturerDao = new ManufacturerDao();

    public ManufacturerService() {
    }

    public List<Manufacturer> findAll() {
        return manufacturerDao.findAll();
    }

    public Manufacturer findOne(final long id) {
        return manufacturerDao.findOne(id);
    }

    public void save(final Manufacturer entity) {
        if (entity == null)
            return;
        manufacturerDao.save(entity);
    }

    public void update(final Manufacturer entity) {
        if (entity == null)
            return;
        manufacturerDao.update(entity);
    }

    public void delete(final Manufacturer entity) {
        if (entity == null)
            return;
        manufacturerDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        manufacturerDao.deleteById(id);
    }
}
