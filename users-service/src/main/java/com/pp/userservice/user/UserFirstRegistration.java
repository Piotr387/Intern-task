package com.pp.userservice.user;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record UserFirstRegistration(
        @NotNull
        @NotBlank
        String login, //
        @NotNull
        @NotBlank
        String email, //
        @Min(value = 6, message = "Password must have at least 6 characters")
        String password //
) {
}
