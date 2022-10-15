package com.pp.email;

import java.time.LocalTime;

record EmailMessageRequest(String login, String email, String lectureName, LocalTime lectureStartingTime) {
}
