package com.example.interntask.service;

import com.example.interntask.entity.LectureEntity;
import com.example.interntask.entity.UserEntity;

public interface EmailService {
    /**
     * This method is responsible for sending email message.
     * For intern task this method will write email message to the end of file "powiadomienia"
     * @param userEntity this param will give us access to email and user that sign up for lecture
     * @param lectureEntity details about lecture that user just sign in
     */
    void sendEmail(UserEntity userEntity, LectureEntity lectureEntity);
}
