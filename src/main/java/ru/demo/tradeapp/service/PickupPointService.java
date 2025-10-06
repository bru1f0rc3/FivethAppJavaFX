package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.PickupPoint;
import ru.demo.tradeapp.repository.PickupPointDao;

import java.util.List;

public class PickupPointService {
    private PickupPointDao pickupPointDao = new PickupPointDao();

    public PickupPointService() {
    }

    public List<PickupPoint> findAll() {
        return pickupPointDao.findAll();
    }

    public PickupPoint findOne(final long id) {
        return pickupPointDao.findOne(id);
    }

    public void save(final PickupPoint entity) {
        if (entity == null)
            return;
        pickupPointDao.save(entity);
    }

    public void update(final PickupPoint entity) {
        if (entity == null)
            return;
        pickupPointDao.update(entity);
    }

    public void delete(final PickupPoint entity) {
        if (entity == null)
            return;
        pickupPointDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        pickupPointDao.deleteById(id);
    }
}
