package com.pp.userservice.lab3.InterfacesSegragation.Segrageted.User;

public interface UserAuthentication {
    String register(String username);
    String login(String username);
}
