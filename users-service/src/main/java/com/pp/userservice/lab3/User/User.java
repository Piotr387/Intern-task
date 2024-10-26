package com.pp.userservice.lab3.User;

import com.pp.userservice.lab3.Lecture.Lecture;
import lombok.Getter;

@Getter
public class User {
    protected String username;

    public User(String username) {
        this.username = username;
    }

    public String attendLecture(Lecture lecture) {
        return username + " is attending " + lecture.getTitle();
    }
}
