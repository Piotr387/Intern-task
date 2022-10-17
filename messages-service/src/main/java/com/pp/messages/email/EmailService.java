package com.pp.messages.email;

import com.pp.messages.file.FileAssistant;
import com.pp.messages.file.FileAssistantTxt;
import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;
import org.springframework.stereotype.Service;

@Service
class EmailService {

    public void send(EmailSignUpConfirmationRequest request) {
        FileAssistant fileAssistant = new FileAssistantTxt();
        fileAssistant.writeToTheEndOfFile(EmailConstants.createConfirmationOfSigningUpForLecture(request));
    }

    public void sendInvitationEmail(EmailRegistrationConfirmRequest request){
        FileAssistant fileAssistant = new FileAssistantTxt();
        fileAssistant.writeToTheEndOfFile(EmailConstants.createConfirmationOfRegistration(request));
    }
}
