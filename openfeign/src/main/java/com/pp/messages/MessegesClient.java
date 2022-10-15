package com.pp.messages;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.pp.messages.EmailConstants.*;

@FeignClient(value = "messages-service")
public interface MessegesClient {

    @PostMapping(EMAIL_ENDPOINT_V1 + SEND_EMAIL_ENDPOINT)
    @ResponseStatus(value = HttpStatus.OK)
    void sendEmail(@RequestBody EmailSignUpConfirmationRequest request);

    @PostMapping(EMAIL_ENDPOINT_V1 + SEND_REGISTRATION_CONFIRM_EMAIL)
    @ResponseStatus(value = HttpStatus.OK)
    void sendRegistrationConfirmEmail(@RequestBody EmailRegistrationConfirmRequest request);
}
