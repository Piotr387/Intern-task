package com.pp.userservice.lab3.User;

import com.pp.userservice.lab3.Lecture.Lecture;

public class RegularUser extends User {
    public RegularUser(String username) {
        super(username);
    }

    @Override
    public String attendLecture(Lecture lecture) {
        super.attendLecture(lecture);
        return username + " joined as a regular user.";
    }
}
