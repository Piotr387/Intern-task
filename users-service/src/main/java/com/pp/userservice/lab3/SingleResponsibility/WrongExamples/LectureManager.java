package com.pp.userservice.lab3.SingleResponsibility.WrongExamples;

import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureAssignment;
import com.pp.userservice.lab3.InterfacesSegragation.Segrageted.Lecture.LectureCreation;
import com.pp.userservice.lab3.Notification.NotificationSender;

class LectureManager {
    private final LectureCreation lectureCreation;
    private final LectureAssignment lectureAssignment;
    private final NotificationSender notificationSender;

    public LectureManager(LectureCreation lectureCreation, LectureAssignment lectureAssignment, NotificationSender notificationSender) {
        this.lectureCreation = lectureCreation;
        this.lectureAssignment = lectureAssignment;
        this.notificationSender = notificationSender;
    }

    public void manageLecture(String title, String teacherName, String userEmail) {
        lectureCreation.createLecture(title);
        lectureAssignment.assignTeacher(title, teacherName);
        notificationSender.sendNotification("Lecture " + title + " has been created and assigned to " + teacherName);
    }
}
