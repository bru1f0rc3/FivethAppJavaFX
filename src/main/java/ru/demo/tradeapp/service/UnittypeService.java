
package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.Unittype;
import ru.demo.tradeapp.repository.UnittypeDao;

import java.util.List;

public class UnittypeService {
    private UnittypeDao unittypeDao = new UnittypeDao();

    public UnittypeService() {
    }

    public List<Unittype> findAll() {
        return unittypeDao.findAll();
    }

    public Unittype findOne(final long id) {
        return unittypeDao.findOne(id);
    }

    public void save(final Unittype entity) {
        if (entity == null)
            return;
        unittypeDao.save(entity);
    }

    public void update(final Unittype entity) {
        if (entity == null)
            return;
        unittypeDao.update(entity);
    }

    public void delete(final Unittype entity) {
        if (entity == null)
            return;
        unittypeDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        unittypeDao.deleteById(id);
    }
}

