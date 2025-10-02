package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.Category;
import ru.demo.tradeapp.model.Manufacturer;

public class ManufacturerDao extends BaseDao<Manufacturer> {
    public ManufacturerDao() {
        super(Manufacturer.class);
    }
}