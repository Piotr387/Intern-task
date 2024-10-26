package com.pp.userservice.lecture.DesignPatterns.Interpreter.Expressions;

import com.pp.userservice.lecture.DesignPatterns.Interpreter.Expression;
import com.pp.userservice.lecture.dto.LectureDTO;

public class OrExpression implements Expression {
    private final Expression expr1;
    private final Expression expr2;

    public OrExpression(Expression expr1, Expression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public boolean interpret(LectureDTO lecture) {
        return expr1.interpret(lecture) || expr2.interpret(lecture);
    }
}
