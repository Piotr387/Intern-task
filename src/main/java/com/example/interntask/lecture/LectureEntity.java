package com.example.interntask.lecture;

import com.example.interntask.user.UserEntity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Class that stores information about structure in databse.
 */

@Entity(name = "lectures")
public class LectureEntity {
    @Transient
    private final int CAPACITY = 5;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;
    private String name;
    @Column(name = "thematic_path")
    private String thematicPath;
    @Column(name = "start_time")
    private LocalTime startTime;
    @ManyToMany(mappedBy = "lectureEntityList")
    private List<UserEntity> userEntityList = new ArrayList<>(CAPACITY);

    public LectureEntity() {
    }

    public LectureEntity(String name, String thematicPath, LocalTime startTime) {
        this.name = name;
        this.thematicPath = thematicPath;
        this.startTime = startTime;
    }

    public int getCAPACITY() {
        return CAPACITY;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThematicPath() {
        return thematicPath;
    }

    public void setThematicPath(String thematicPath) {
        this.thematicPath = thematicPath;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public List<UserEntity> getUserEntityList() {
        return userEntityList;
    }

    public void setUserEntityList(List<UserEntity> userEntityList) {
        this.userEntityList = userEntityList;
    }

    public int getUserEntityListSize(){
        return userEntityList.size();
    }
}
