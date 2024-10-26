package com.pp.userservice.lab3.Lecture;

public class OnlineLecture extends Lecture {
    public OnlineLecture(String title) {
        super(title);
    }

    @Override
    public String getTitle() {
        return super.getTitle() + " (Online)";
    }
}
