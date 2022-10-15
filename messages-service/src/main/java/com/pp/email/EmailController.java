package com.pp.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = EmailController.EMAIL_ENDPOINT_V1)
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    static final String EMAIL_ENDPOINT_V1 = "api/v1/email";
    private final EmailService emailService;

    @PostMapping("/confirmation-sign-up")
    @ResponseStatus(value = HttpStatus.OK)
    public void sendEmail(@RequestBody EmailMessageRequest emailMessageRequest){
        log.info("Sending new email in progress {}", emailMessageRequest);
        emailService.send(emailMessageRequest);
        log.info("Email message has been sent! {}", emailMessageRequest);
    }

    @PostMapping("/registration")
    @ResponseStatus(value = HttpStatus.OK)
    public void sendRegistrationConfirmEmail(@RequestBody EmailRegistrationRequest request){
        log.info("Sending new registration email in progress {}", request);
        emailService.sendInvitationEmail(request);
        log.info("Registration Email message has been sent! {}", request);
    }
}
