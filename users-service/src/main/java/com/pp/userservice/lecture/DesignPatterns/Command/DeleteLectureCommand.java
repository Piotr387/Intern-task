package com.pp.userservice.lecture.DesignPatterns.Command;

import com.pp.userservice.lecture.service.LectureService;

import javax.inject.Inject;

public class DeleteLectureCommand implements Command {
    @Inject
    private final LectureService lectureService;
    private final String thematicPath;


    public DeleteLectureCommand(LectureService lectureService, String lectureName) {
        this.lectureService = lectureService;
        this.thematicPath = lectureName;
    }

    @Override
    public void execute() {
        lectureService.deleteLecture(thematicPath);
    }
}
