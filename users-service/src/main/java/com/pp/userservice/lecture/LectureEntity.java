package com.pp.userservice.lecture;

import com.pp.userservice.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class that stores information about structure in databse.
 */

@Entity(name = "lectures")
@Getter
@Setter
@NoArgsConstructor
public class LectureEntity implements Serializable {
    private static final int MAX_NUMBER_OF_ATTENDANTS = 5;

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    //Magic number removal
    @Transient
    private static final int CAPACITY = MAX_NUMBER_OF_ATTENDANTS;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String name;
    @Column(name = "thematic_path")
    private String thematicPath;
    @Column(name = "start_time")
    private LocalTime startTime;
    @ManyToMany(mappedBy = "lectureEntityList", cascade = CascadeType.ALL)
    private List<UserEntity> userEntityList = new ArrayList<>(CAPACITY);

    public LectureEntity(String name, String thematicPath, LocalTime startTime) {
        this.name = name;
        this.thematicPath = thematicPath;
        this.startTime = startTime;
    }

    public int getUserEntityListSize() {
        return userEntityList.size();
    }

    public int getCAPACITY() {
        return CAPACITY;
    }
}
