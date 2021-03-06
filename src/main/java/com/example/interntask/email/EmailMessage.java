package com.example.interntask.email;

import com.example.interntask.lecture.LectureEntity;
import com.example.interntask.user.UserEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailMessage {

    private EmailMessage() {
    }

    /**
     * Method to create message that going to be send to user after sign in to lecture
     * @param userEntity to get user login and user name
     * @param lectureEntity to get details about lecture that user has signed in
     * @return value is string message create from above data
     */
    public static String createMessage(UserEntity userEntity, LectureEntity lectureEntity){
        String dateOfSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "Dziękujemy za rejestracje " + userEntity.getLogin() + ", " +
                "zostałeś pomyślnie zapisany na " + lectureEntity.getName() + " " +
                "start wydarzenia " + lectureEntity.getStartTime() + " pod koniec prelekcji " +
                "przewidziana przerwa na kawę";
        return dateOfSend + ", " + userEntity.getEmail() + ", " + message + "\n";
    }

    public static String sendRegistrationEmail(UserEntity userEntity, String password){
        String dateOfSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "Dziękujemy za rejestracje w naszym systemie " + userEntity.getLogin() + ", " +
                "Od teraz zapisywać się na kolejne prelekcje możes z poziomu konta. " +
                "Posiadając konto możesz zarządzać swoimi rezerwacjami. Twoje dane logowania: " +
                "Login: " + userEntity.getLogin() + " hasło: " + password;
        return dateOfSend + ", " + userEntity.getEmail() + ", " + message + "\n";
    }
}
