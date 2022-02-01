package com.fairycompany.reviewer.controller;

import com.fairycompany.reviewer.controller.command.*;
import com.fairycompany.reviewer.exception.CommandException;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Servlet that processes http requests
 */
@WebServlet(name = "ControllerServlet", value = "/controller")
public class ControllerServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Executes command from request parameter and forwards or redirect to received jsp page
     *
     * @param request the http request
     * @param response the http response
     * @throws IOException
     * @throws ServletException
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Command command = (Command) request.getAttribute(RequestAttribute.COMMAND);

        try {
            Router router = command.execute(request);

            switch (router.getType()) {
                case FORWARD -> request.getRequestDispatcher(router.getPage()).forward(request, response);
                case REDIRECT -> response.sendRedirect(request.getContextPath() + router.getPage());
                default -> {
                    logger.log(Level.ERROR, "Router type {} is incorrect", router.getType());
                    response.sendRedirect(request.getContextPath() + PagePath.ERROR_404);
                }
            }
        } catch (CommandException e) {
            logger.log(Level.ERROR, "Error when executing command {} ", command.getClass().getName());
            request.getSession().setAttribute(SessionAttribute.EXCEPTION, e);
            response.sendRedirect(request.getContextPath() + PagePath.EXCEPTION_ERROR_REDIRECT);
        }
    }
}
