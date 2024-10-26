package com.pp.userservice.lecture.DesignPatterns.Command;

import lombok.Setter;

@Setter
public class LectureDeletingInvoker {
    private Command command;

    public void execute() {
        this.command.execute();
    }
}
