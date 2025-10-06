package ru.demo.tradeapp.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "status", schema = "public")
public class Status {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    @OneToMany
    @JoinColumn(name = "status_id")
    private Set<Order> orders = new HashSet<Order>();

    public Status() {

    }

    public Status(Long statusId, String title) {
        this.statusId = statusId;
        this.title = title;

    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status status)) return false;
        return Objects.equals(statusId, status.statusId) && Objects.equals(title, status.title);
    }

    @Override
    public int hashCode() {
        final int hashCode = 17 * statusId.hashCode() + 31 * title.hashCode();
        return hashCode;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
