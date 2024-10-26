package com.pp.userservice.lab3.Syllabus;

import lombok.Getter;

@Getter
public class Syllabus {
    protected String title;
    protected String description;

    public Syllabus(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String displayContent() {
        return "Syllabus: " + title + "Description: " + description;
    }
}
