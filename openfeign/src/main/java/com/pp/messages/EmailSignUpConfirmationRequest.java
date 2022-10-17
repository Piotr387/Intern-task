package com.pp.messages;

public record EmailSignUpConfirmationRequest(String login, String email, String lectureName, String lectureStartingTime) {
}
