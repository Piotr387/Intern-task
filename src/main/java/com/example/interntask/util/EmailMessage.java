package com.example.interntask.util;

import com.example.interntask.entity.LectureEntity;
import com.example.interntask.entity.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailMessage {

    public static String createMessage(UserEntity userEntity, LectureEntity lectureEntity){
        String dateOfSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "Dziękujemy za rejestracje " + userEntity.getLogin() + ", " +
                "zostałeś pomyślnie zapisany na " + lectureEntity.getName() + " " +
                "start wydarzenia " + lectureEntity.getStartTime() + " pod koniec prelekcji " +
                "przewidziana przerwa na kawę";
        return dateOfSend + ", " + userEntity.getEmail() + ", " + message + "\n";
    }
}
