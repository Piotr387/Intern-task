package com.pp.userservice.lab3.InterfacesSegragation.FatInterfaces;

public interface LectureManagement {
    void createLecture(String title);
    void cancelLecture(String title);
    void assignTeacher(String lectureTitle, String teacherName);
    void markAttendance(String lectureTitle, String username);
}
