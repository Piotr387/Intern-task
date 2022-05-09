package com.example.interntask.email;

import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.user.UserEntity;

public interface EmailService {
    /**
     * This method is responsible for sending email message.
     * For intern task this method will write email message to the end of file "powiadomienia"
     * @param userEntity this param will give us access to email and user that sign up for lecture
     * @param lectureEntity details about lecture that user just sign in
     */
    void sendEmail(UserEntity userEntity, LectureEntity lectureEntity);
}
