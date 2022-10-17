package com.pp.userservice.user;

import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.role.RoleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class that stores information about user data structure in databse
 * Default value: roleEntities = ROLE_USER
 */

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4928574404969965424L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(name = "encrypted_password")
    private String encryptedPassword;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "user_lecture",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "lecture_id")}
    )

    private List<LectureEntity> lectureEntityList = new ArrayList<>(3);

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roles = new ArrayList<>();

    public UserEntity(String login, String email) {
        this(login, email, "123");
    }

    public UserEntity(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.encryptedPassword = password;
    }

}
