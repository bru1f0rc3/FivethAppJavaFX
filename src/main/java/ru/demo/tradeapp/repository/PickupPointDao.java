package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.PickupPoint;

public class PickupPointDao extends BaseDao<PickupPoint> {
    public PickupPointDao() {
        super(PickupPoint.class);
    }
}
