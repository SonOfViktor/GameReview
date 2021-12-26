package com.fairycompany.reviewer.controller;

import com.fairycompany.reviewer.controller.command.LocaleMessageKey;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import com.fairycompany.reviewer.controller.command.SessionAttribute;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/upload_image")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 25)
public class ImageServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String commandName = request.getParameter(RequestParameter.COMMAND);
        HttpSession session = request.getSession();
        if (commandName != null) {
            request.getRequestDispatcher("/controller").forward(request, response);
        } else {
            logger.log(Level.WARN, "The user is trying to upload a file size that is too large");
            session.setAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.FILE_BIG_SIZE);
            response.sendRedirect(request.getContextPath() + session.getAttribute(SessionAttribute.CURRENT_PAGE));
        }
    }
}
