package com.pp.userservice.lab3.Syllabus;

public class AdvancedSyllabus extends Syllabus {
    public AdvancedSyllabus() {
        super("Advanced Course Outline", "Covers advanced topics and projects.");
    }

    @Override
    public String displayContent() {
        return "Advanced Syllabus: " + title + "Description: " + description;
    }
}
