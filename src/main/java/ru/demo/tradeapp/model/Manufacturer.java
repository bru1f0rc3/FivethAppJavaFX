package ru.demo.tradeapp.model;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "manufacturer", schema = "public")
public class Manufacturer {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long manufacturerId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;


    public Manufacturer() {

    }

    public Manufacturer(Long manufacturerId, String title) {
        this.manufacturerId = manufacturerId;
        this.title = title;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Manufacturer that)) return false;
        return Objects.equals(manufacturerId, that.manufacturerId) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        final int hashCode = 17 * manufacturerId.hashCode() + 31 * title.hashCode();
        return hashCode;
    }

    public Long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}



