package com.pp.userservice.lecture.dto;

import com.pp.userservice.user.api.UserLoginWithEmail;

import java.util.List;

public record LectureDetailsWithUser(
        String id, //
        String name, //
        String startTime, //
        String thematicPath, //
        List<UserLoginWithEmail> users //
) {
}
