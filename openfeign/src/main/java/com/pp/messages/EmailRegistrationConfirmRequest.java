package com.pp.messages;

public record EmailRegistrationConfirmRequest(String login, String email, String password) {
}
