package com.pp.userservice.lecture.DesignPatterns.Iterator;

import com.pp.userservice.lecture.dto.LectureDTO;

import java.util.ArrayList;
import java.util.List;

public class LectureCollection implements Aggregate<LectureDTO>{
    private List<LectureDTO> lectures = new ArrayList<>();

    public void addLecture(LectureDTO lecture) {
        lectures.add(lecture);
    }

    public LectureCollection(List<LectureDTO> lectures){
        this.lectures = lectures;
    }

    @Override
    public Iterator<LectureDTO> createIterator() {
        return new LectureIterator(lectures);
    }
}
