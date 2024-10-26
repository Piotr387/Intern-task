package com.pp.userservice.lab3.Lecture;

public class InPersonLecture extends Lecture {
    public InPersonLecture(String title) {
        super(title);
    }

    @Override
    public String getTitle() {
        return super.getTitle() + " (In Person)";
    }
}
