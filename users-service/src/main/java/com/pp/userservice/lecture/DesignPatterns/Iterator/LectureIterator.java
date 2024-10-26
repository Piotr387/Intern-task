package com.pp.userservice.lecture.DesignPatterns.Iterator;

import com.pp.userservice.lecture.dto.LectureDTO;

import java.util.List;

public class LectureIterator implements Iterator<LectureDTO> {
    public List<LectureDTO> lectures;
    private int position = 0;

    public LectureIterator(List<LectureDTO> lectures) {
        this.lectures = lectures;
    }

    @Override
    public boolean hasNext() {
        return position < lectures.size();
    }

    @Override
    public LectureDTO next() {
        return lectures.get(position++);
    }
}
