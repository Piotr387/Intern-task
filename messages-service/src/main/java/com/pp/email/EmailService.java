package com.pp.email;

import com.pp.file.FileAssistant;
import com.pp.file.FileAssistantTxt;
import org.springframework.stereotype.Service;

@Service
class EmailService {

    public void send(EmailMessageRequest emailMessageRequest) {
        FileAssistant fileAssistant = new FileAssistantTxt();
        fileAssistant.writeToTheEndOfFile(EmailConstants.createConfirmationOfSigningUpForLecture(emailMessageRequest));
    }

    public void sendInvitationEmail(EmailRegistrationRequest request){
        FileAssistant fileAssistant = new FileAssistantTxt();
        fileAssistant.writeToTheEndOfFile(EmailConstants.createConfirmationOfRegistration(request));
    }
}
