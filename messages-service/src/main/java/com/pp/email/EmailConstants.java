package com.pp.email;

import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EmailConstants {

    static String createConfirmationOfSigningUpForLecture(EmailSignUpConfirmationRequest request) {
        String dateOfSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "Dziękujemy za rejestracje " + request.login() + ", " +
                "zostałeś pomyślnie zapisany na " + request.lectureName() + " " +
                "start wydarzenia " + request.lectureStartingTime() + " pod koniec prelekcji " +
                "przewidziana przerwa na kawę";
        return dateOfSend + ", " + request.email() + ", " + message + "\n";
    }

    public static String createConfirmationOfRegistration(EmailRegistrationConfirmRequest request) {
        String dateOfSend = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String message = "Dziękujemy za rejestracje w naszym systemie " + request.login() + ", " +
                "Od teraz zapisywać się na kolejne prelekcje możes z poziomu konta. " +
                "Posiadając konto możesz zarządzać swoimi rezerwacjami. Twoje dane logowania: " +
                "Login: " + request.login() + " hasło: " + request.password();
        return dateOfSend + ", " + request.email() + ", " + message + "\n";
    }
}
