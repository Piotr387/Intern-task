package com.pp.userservice.lab3.User;

import com.pp.userservice.lab3.Lecture.Lecture;

public class AdminUser extends User {
    public AdminUser(String username) {
        super(username);
    }

    @Override
    public String attendLecture(Lecture lecture) {
        super.attendLecture(lecture);
        return username + " joined as an admin user with extra permissions.";
    }
}
