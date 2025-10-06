package ru.demo.tradeapp.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "unittype", schema = "public")
public class Unittype {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long unittypeId;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    public Unittype() {

    }

    public Unittype(Long unittypeId, String title) {
        this.unittypeId = unittypeId;
        this.title = title;

    }

    public Long getUnittypeId() {
        return unittypeId;
    }

    public void setUnittypeId(Long categoryId) {
        this.unittypeId = unittypeId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Unittype unittype)) return false;
        return Objects.equals(unittypeId, unittype.unittypeId) && Objects.equals(title, unittype.title);
    }

    @Override
    public int hashCode() {
        final int hashCode = 17 * unittypeId.hashCode() + 31 * title.hashCode();
        return hashCode;
    }
}