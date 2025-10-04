package ru.demo.tradeapp.model;

import jakarta.persistence.*;
import org.hibernate.Session;
import ru.demo.tradeapp.util.HibernateSessionFactoryUtil;

@Entity
@Table(name = "role", schema = "public")
public class Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    public Role(){

    }

    public Role(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
