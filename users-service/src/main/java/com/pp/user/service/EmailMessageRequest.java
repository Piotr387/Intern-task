package com.pp.user.service;

import java.time.LocalTime;

record EmailMessageRequest(String login, String email, String lectureName, LocalTime lectureStartingTime) {
}
