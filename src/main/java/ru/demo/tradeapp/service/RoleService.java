package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.Role;
import ru.demo.tradeapp.repository.RoleDao;

import java.util.List;

public class RoleService {
    private RoleDao roleDao = new RoleDao();

    public RoleService() {
    }

    public List<Role> findAll() {
        return roleDao.findAll();
    }

    public Role findOne(final long id) {
        return roleDao.findOne(id);
    }

    public void save(final Role entity) {
        if (entity == null)
            return;
        roleDao.save(entity);
    }

    public void update(final Role entity) {
        if (entity == null)
            return;
        roleDao.update(entity);
    }

    public void delete(final Role entity) {
        if (entity == null)
            return;
        roleDao.delete(entity);
    }

    public void deleteById(final Long id) {
        if (id == null)
            return;
        roleDao.deleteById(id);
    }
}
