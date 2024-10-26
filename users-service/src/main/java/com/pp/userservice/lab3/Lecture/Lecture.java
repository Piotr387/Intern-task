package com.pp.userservice.lab3.Lecture;

import com.pp.userservice.lab3.Syllabus.Syllabus;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Lecture {
    protected String title;
    @Setter
    protected Syllabus syllabus;

    public Lecture(String title) {
        this.title = title;
    }

    public String showSyllabus() {
        if (syllabus != null) {
            return syllabus.displayContent();
        } else {
            return "No syllabus assigned to " + title;
        }
    }

}
