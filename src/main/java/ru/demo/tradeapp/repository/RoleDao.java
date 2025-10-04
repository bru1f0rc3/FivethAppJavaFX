package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.Role;

public class RoleDao extends BaseDao<Role> {
    public RoleDao() {
        super(Role.class);
    }
}