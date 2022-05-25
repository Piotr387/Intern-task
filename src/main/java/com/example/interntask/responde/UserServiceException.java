package com.example.interntask.responde;

public class UserServiceException extends RuntimeException{

    private static final long serialVersionUID = -2041518908692689138L;

    public UserServiceException(String message) {
        super(message);
    }
}
