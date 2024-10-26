package com.pp.userservice.lecture.DesignPatterns.Interpreter.Expressions;

import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expression;
import com.pp.userservice.lecture.dto.LectureDTO;

import java.time.LocalTime;

public class StartTimeExpression implements Expression {
    private final String startTime;

    public StartTimeExpression(String startTime) {
        this.startTime = startTime;
    }

    public boolean interpret(LectureDTO lecture) {
        return lecture.getStartTime().equals(LocalTime.parse(startTime));
    }
}
