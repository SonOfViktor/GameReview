package com.fairycompany.reviewer.controller.command;

import com.fairycompany.reviewer.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The interface Command.
 */
public interface Command {
    /**
     * Method is called by controller to execute command of request.
     *
     * @param request servlet request object
     * @return router object that contains page path and routing type
     * @throws CommandException if command exception occurred
     */
    Router execute(HttpServletRequest request) throws CommandException;
}
