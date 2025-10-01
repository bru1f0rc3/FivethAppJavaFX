package ru.demo.tradeapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role", schema = "public")
public class Role {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        if (getId() == 1)
            title = "Клиент";
        if (getId() == 2)
            title = "Администратор";
        if (getId() == 3)
            title = "Менеджер";
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
