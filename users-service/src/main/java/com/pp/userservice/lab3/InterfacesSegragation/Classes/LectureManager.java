package com.pp.userservice.lab3.InterfacesSegragation.Classes;

import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureAssignment;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureCreation;

public class LectureManager implements LectureCreation, LectureAssignment {
    @Override
    public String createLecture(String title) {
        return "Creating lecture: " + title;
    }

    @Override
    public String cancelLecture(String title) {
        return "Cancelling lecture: " + title;
    }

    @Override
    public String assignTeacher(String lectureTitle, String teacherName) {
        return "Assigning teacher " + teacherName + " to lecture: " + lectureTitle;
    }
}
