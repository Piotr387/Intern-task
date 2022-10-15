package com.pp.role;

import com.pp.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class RoleEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -1209289997505536602L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users;

    public RoleEntity(String name) {
        this.name = name;
    }
}
