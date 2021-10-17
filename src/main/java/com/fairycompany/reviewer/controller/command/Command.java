package com.fairycompany.reviewer.controller.command;

import com.fairycompany.reviewer.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    Router execute(HttpServletRequest request) throws CommandException;
}
