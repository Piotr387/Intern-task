package com.example.interntask.role;

import com.example.interntask.user.UserEntity;

import javax.persistence.*;
import java.util.List;

@Entity
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    @ManyToMany(mappedBy = "roleEntities")
    private List<UserEntity> users;

    public RoleEntity() {
    }

    public RoleEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}
