package com.example.interntask.email;

import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.user.UserEntity;
import com.example.interntask.file.FileAssistant;
import com.example.interntask.file.FileAssistantTxt;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImplementation implements EmailService {

    @Override
    public void sendEmail(UserEntity userEntity, LectureEntity lectureEntity) {
        FileAssistant fileAssistant = new FileAssistantTxt();
        fileAssistant.writeToTheEndOfFile(EmailMessage.createMessage(userEntity,lectureEntity));
    }

    @Override
    public void sendInvitationEmail(UserEntity userEntity, String password) {
        FileAssistant fileAssistant = new FileAssistantTxt();
        fileAssistant.writeToTheEndOfFile(EmailMessage.sendRegistrationEmail(userEntity,password));
    }
}
