package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.User;

public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(User.class);
    }
}
