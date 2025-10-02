package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.Category;

public class CategoryDao extends BaseDao<Category> {
    public CategoryDao() {
        super(Category.class);
    }
}
