package ru.demo.tradeapp.repository;

import org.hibernate.Session;
import ru.demo.tradeapp.model.User;

import java.util.List;

public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(User.class);
    }

    @Override
    public List<User> findAll() {
        Session session = getCurrentSession();
        session.beginTransaction();
        List<User> items = (List<User>) session.createQuery("from " + clazz.getName() + " join fetch role").list();
        session.close();
        return items;
    }

}
