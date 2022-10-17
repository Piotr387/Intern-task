package com.pp.messages.email;

import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
class EmailService {

    private static final String FROM_WHO = "inser@email.com";
    private static final String SUBJECT_INVITATION_EMAIL = "Pomyślna rejestracja";
    private static final String SUBJECT_CONFIRMATION_EMAIL = "Pomyślne zapisanie sie na prelekcje";

    public void send(EmailSignUpConfirmationRequest request) {
        var message = EmailConstants.createConfirmationOfSigningUpForLecture(request);
        sendEmailViaLocalServer(request.email(), SUBJECT_CONFIRMATION_EMAIL, message);
    }

    public void sendInvitationEmail(EmailRegistrationConfirmRequest request){
        var message = EmailConstants.createConfirmationOfRegistration(request);
        sendEmailViaLocalServer(request.email(), SUBJECT_INVITATION_EMAIL, message);
    }

    @SneakyThrows
    private void sendEmailViaLocalServer(String email, String subject, String messageText){
        // Assuming you are sending email from localhost
        String host = "localhost";
        // Assuming you are sending email from localhost.
        String port = "25";
        // Get system properties
        Properties properties = System.getProperties();
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        // Setup mail server.
        properties.setProperty("mail.smtp.port", port);
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(FROM_WHO));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            // Set Subject: header field
            message.setSubject(subject);
            // Now set the actual message
            message.setText(messageText);
            // Send message
            Transport.send(message);
            log.info("Message sent successfully to {}", email);
        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
