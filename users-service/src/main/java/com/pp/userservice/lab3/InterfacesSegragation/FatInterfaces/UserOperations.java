package com.pp.userservice.lab3.InterfacesSegragation.FatInterfaces;

public interface UserOperations {
    void register(String username);
    void login(String username);
    void subscribeToLecture(String lectureTitle);
    void unsubscribeFromLecture(String lectureTitle);
}
