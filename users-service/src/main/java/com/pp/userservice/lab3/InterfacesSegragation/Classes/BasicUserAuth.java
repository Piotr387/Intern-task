package com.pp.userservice.lab3.InterfacesSegragation.Classes;

import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.User.UserAuthentication;

public class BasicUserAuth implements UserAuthentication {
    @Override
    public String register(String username) {
        return "Registering user: " + username;
    }

    @Override
    public String login(String username) {
        return "Logging in user: " + username;
    }
}
