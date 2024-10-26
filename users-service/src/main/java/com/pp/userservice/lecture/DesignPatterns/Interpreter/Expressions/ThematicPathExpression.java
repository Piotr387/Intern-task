package com.pp.userservice.lecture.DesignPatterns.Interpreter.Expressions;

import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expression;
import com.pp.userservice.lecture.dto.LectureDTO;

public class ThematicPathExpression implements Expression {
    private final String path;

    public ThematicPathExpression(String path) {
        this.path = path;
    }

    public boolean interpret(LectureDTO lecture) {
        return lecture.getThematicPath().equals(path);
    }
}
