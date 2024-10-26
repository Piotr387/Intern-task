package com.pp.userservice.lab3.SingleResponsibility.CorectedExamples;

import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureAssignment;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureCreation;
import com.pp.userservice.lab3.Notification.NotificationSender;

//l3z6
public class LectureManager {
    private final LectureCreation lectureCreation;
    private final LectureAssignment lectureAssignment;
    private final NotificationSender notificationSender;

    public LectureManager(LectureCreation lectureCreation, LectureAssignment lectureAssignment, NotificationSender notificationSender) {
        this.lectureCreation = lectureCreation;
        this.lectureAssignment = lectureAssignment;
        this.notificationSender = notificationSender;
    }

    public void createLecture(String title) {
        lectureCreation.createLecture(title);
    }

    public void assignTeacher(String title, String teacherName) {
        lectureAssignment.assignTeacher(title, teacherName);
    }

    public void sendNotification(String title, String teacherName) {
        notificationSender.sendNotification("Lecture " + title + " has been created and assigned to " + teacherName);
    }

    public void manageLecture(String title, String teacherName, String userEmail) {
        createLecture(title);
        assignTeacher(title, teacherName);
        sendNotification(title, teacherName);
    }
}
