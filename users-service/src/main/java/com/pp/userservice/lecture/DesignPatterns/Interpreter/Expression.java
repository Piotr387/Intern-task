package com.pp.userservice.lecture.DesignPatterns.Interpreter;

import com.pp.userservice.lecture.dto.LectureDTO;

public interface Expression {
    boolean interpret(LectureDTO lecture);
}
