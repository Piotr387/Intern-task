package com.pp.userservice.user.entity;

import com.pp.userservice.common.HistoryEntityListener;
import com.pp.userservice.common.HistoryFieldsEntityTracker;
import com.pp.userservice.lecture.LectureEntity;
import com.pp.userservice.role.RoleEntity;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class that stores information about user data structure in databse
 * Default value: roleEntities = ROLE_USER
 */

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(HistoryEntityListener.class)
public class UserEntity extends HistoryFieldsEntityTracker<UserEntity> {
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
