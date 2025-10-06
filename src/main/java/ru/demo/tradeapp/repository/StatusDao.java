package ru.demo.tradeapp.repository;

import ru.demo.tradeapp.model.Status;

public class StatusDao extends BaseDao<Status> {
    public StatusDao() {
        super(Status.class);
    }
}
