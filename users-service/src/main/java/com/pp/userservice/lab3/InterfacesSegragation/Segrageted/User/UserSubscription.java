package com.pp.userservice.lab3.InterfacesSegragation.Segrageted.User;

public interface UserSubscription {
    String subscribeToLecture(String lectureTitle);
    String unsubscribeFromLecture(String lectureTitle);
}
