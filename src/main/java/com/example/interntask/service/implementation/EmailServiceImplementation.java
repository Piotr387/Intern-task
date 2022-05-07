package com.example.interntask.service.implementation;

import com.example.interntask.entity.LectureEntity;
import com.example.interntask.entity.UserEntity;
import com.example.interntask.service.EmailService;
import com.example.interntask.util.EmailMessage;
import com.example.interntask.util.FileAssistant;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {

    @Override
    public void sendEmail(UserEntity userEntity, LectureEntity lectureEntity) {
        FileAssistant.writeToTheEndOfFile(EmailMessage.createMessage(userEntity,lectureEntity));
    }
}
