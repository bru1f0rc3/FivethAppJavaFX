package ru.demo.tradeapp.model;

import jakarta.persistence.*;
import org.hibernate.Session;
import ru.demo.tradeapp.util.HibernateSessionFactoryUtil;

@Entity
@Table(name = "role", schema = "public")
public class Role {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    private String loadTitleRoleName(){
        String title = "";
        try(Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()){
            Query query = session.createQuery("from Role where roleId = :roleId");
            query.setParameter("roleId", getId());
            Role role = (Role) query.getSingleResult();
            title = role.getTitle();
        }
        catch (Exception e){
            System.out.println("Исключение " + e);
        }
        return title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        if (this.title == null) {
            this.title = loadTitleRoleName();
        }
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
