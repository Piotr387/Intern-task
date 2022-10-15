package com.pp.messages;

import java.time.LocalTime;

public record EmailSignUpConfirmationRequest(String login, String email, String lectureName, LocalTime lectureStartingTime) {
}
