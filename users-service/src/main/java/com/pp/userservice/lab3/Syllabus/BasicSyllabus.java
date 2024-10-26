package com.pp.userservice.lab3.Syllabus;

public class BasicSyllabus extends Syllabus {
    public BasicSyllabus() {
        super("Basic Course Outline", "Covers introductory topics.");
    }

    @Override
    public String displayContent() {
        return "Basic Syllabus: " + title + "Description: " + description;
    }
}
