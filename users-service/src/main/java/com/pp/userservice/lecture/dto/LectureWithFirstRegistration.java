package com.pp.userservice.lecture.dto;

import com.pp.userservice.user.UserFirstRegistration;

public record LectureWithFirstRegistration(
        String lectureName, //

        UserFirstRegistration userDTO //
) {
}
