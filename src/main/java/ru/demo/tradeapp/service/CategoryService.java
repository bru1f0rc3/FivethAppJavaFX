package ru.demo.tradeapp.service;

import ru.demo.tradeapp.model.Category;
import ru.demo.tradeapp.repository.CategoryDao;

import java.util.List;

public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();

    public CategoryService() {
    }

    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    public Category findOne(final long id) {
        return categoryDao.findOne(id);
    }

    public void save(final Category entity)
    {
        if (entity == null)
            return;
        categoryDao.save(entity);
    }

    public void update(final Category entity)
    {
        if (entity == null)
            return;
        categoryDao.update(entity);
    }

    public void delete(final Category entity)
    {
        if (entity == null)
            return;
        categoryDao.delete(entity);
    }

    public void deleteById(final Long id)
    {
        if (id == null)
            return;
        categoryDao.deleteById(id);
    }
}
