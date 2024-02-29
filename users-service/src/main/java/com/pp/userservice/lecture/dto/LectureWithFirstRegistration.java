package com.pp.userservice.lecture.dto;

import com.pp.userservice.user.UserFirstRegistration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record LectureWithFirstRegistration(
        String lectureName, //
        @Valid
        @NotNull
        UserFirstRegistration userDTO //
) {
}
