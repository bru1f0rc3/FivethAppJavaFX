package ru.demo.tradeapp.model;

import jakarta.persistence.*;

import java.util.Objects;


@Entity
@Table(name = "pickup_point", schema = "public")
public class PickupPoint {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pickupPointId;
    @Column(name = "address", nullable = false)
    private String address;

    public PickupPoint() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PickupPoint that)) return false;
        return Objects.equals(pickupPointId, that.pickupPointId) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        final int hashCode = 17 * pickupPointId.hashCode() + 31 * address.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return address;
    }

    public Long getPickupPointId() {
        return pickupPointId;
    }

    public void setPickupPointId(Long pickupPointId) {
        this.pickupPointId = pickupPointId;
    }

    public String getTitle() {
        return address;
    }

    public void setTitle(String address) {
        this.address = address;
    }
}
