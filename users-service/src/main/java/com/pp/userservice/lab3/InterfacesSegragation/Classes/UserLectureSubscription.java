package com.pp.userservice.lab3.InterfacesSegragation.Classes;

import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.User.UserSubscription;

public class UserLectureSubscription implements UserSubscription {
    @Override
    public String subscribeToLecture(String lectureTitle) {
        return "Subscribing user to lecture: " + lectureTitle;
    }

    @Override
    public String unsubscribeFromLecture(String lectureTitle) {
        return "Unsubscribing user from lecture: " + lectureTitle;
    }
}
