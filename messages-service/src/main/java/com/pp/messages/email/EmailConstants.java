package com.pp.messages.email;

import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EmailConstants {

    static String createConfirmationOfSigningUpForLecture(EmailSignUpConfirmationRequest request) {
        String message = "Dziękujemy za rejestracje " + request.login() + ", \n" +
                "zostałeś pomyślnie zapisany na " + request.lectureName() + " " +
                "\nStart wydarzenia " + request.lectureStartingTime() + " pod koniec prelekcji " +
                "przewidziana przerwa na kawę";
        return message + "\n";
    }

    public static String createConfirmationOfRegistration(EmailRegistrationConfirmRequest request) {
        String message = "Dziękujemy za rejestracje w naszym systemie " + request.login() + ", \n" +
                "Od teraz zapisywać się na kolejne prelekcje możes z poziomu konta. \n" +
                "Posiadając konto możesz zarządzać swoimi rezerwacjami. \nTwoje dane logowania: " +
                "Login: " + request.login() + " hasło: " + request.password();
        return message + "\n";
    }
}
