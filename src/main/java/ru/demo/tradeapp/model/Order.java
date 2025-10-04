package ru.demo.tradeapp.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders", schema = "public")

public class Order {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "status_id", nullable = false)
    private Long statusId;

    @Column(name = "pickuppoint_id", nullable = false)
    private Long pickuppointId;

    @Column(name = "create_date", nullable = false)
    private LocalDate create_date;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "username", nullable = true, length = 50)
    private Long username;

    @Column(name = "get_code", nullable = false)
    private Integer getCode;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getPickuppointId() {
        return pickuppointId;
    }

    public void setPickuppointId(Long pickuppointId) {
        this.pickuppointId = pickuppointId;
    }

    public LocalDate getCreate_date() {
        return create_date;
    }

    public void setCreate_date(LocalDate create_date) {
        this.create_date = create_date;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getUsername() {
        return username;
    }

    public void setUsername(Long username) {
        this.username = username;
    }

    public Integer getGetCode() {
        return getCode;
    }

    public void setGetCode(Integer getCode) {
        this.getCode = getCode;
    }
}
