package com.pp.userservice.lecture.dto;

import com.pp.userservice.user.UserLoginWithEmail;

import java.util.List;

public record LectureDetailsWithUser(
        String id, //
        String name, //
        String startTime, //
        String thematicPath, //
        List<UserLoginWithEmail> users //
) {
}
