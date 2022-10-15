package com.pp.messages;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailConstants {

    public static final String EMAIL_ENDPOINT_V1 = "api/v1/email";
    public static final String SEND_EMAIL_ENDPOINT = "/confirmation-success";
    public static final String SEND_REGISTRATION_CONFIRM_EMAIL = "/registration-success";
}
