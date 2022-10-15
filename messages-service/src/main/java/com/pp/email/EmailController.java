package com.pp.email;

import com.pp.messages.EmailRegistrationConfirmRequest;
import com.pp.messages.EmailSignUpConfirmationRequest;
import com.pp.messages.MessegesClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.pp.messages.EmailConstants.*;

@RestController
@RequestMapping(value = EMAIL_ENDPOINT_V1)
@RequiredArgsConstructor
@Slf4j
public class EmailController implements MessegesClient {
    private final EmailService emailService;

    @Override
    @PostMapping(SEND_EMAIL_ENDPOINT)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendEmail(@RequestBody EmailSignUpConfirmationRequest request){
        log.info("Sending new email in progress {}", request);
        emailService.send(request);
        log.info("Email message has been sent! {}", request);
    }

    @Override
    @PostMapping(SEND_REGISTRATION_CONFIRM_EMAIL)
    @ResponseStatus(value = HttpStatus.OK)
    public void sendRegistrationConfirmEmail(@RequestBody EmailRegistrationConfirmRequest request){
        log.info("Sending new registration email in progress {}", request);
        emailService.sendInvitationEmail(request);
        log.info("Registration Email message has been sent! {}", request);
    }
}
