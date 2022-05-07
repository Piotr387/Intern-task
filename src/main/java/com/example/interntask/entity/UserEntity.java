package com.example.interntask.entity;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity(name = "users")
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4928574404969965424L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String login;
    private String email;
    @ManyToMany(cascade = { CascadeType.PERSIST })
    @JoinTable(
            name = "user_lecture",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "lecture_id") }
    )
    private List<LectureEntity> lectureEntityList = new ArrayList<>(3);

    public UserEntity() {
    }

    public UserEntity(String login, String email) {
        this.login = login;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<LectureEntity> getLectureEntityList() {
        return lectureEntityList;
    }

    public void setLectureEntityList(List<LectureEntity> lectureEntityList) {
        this.lectureEntityList = lectureEntityList;
    }
}
