package com.fairycompany.reviewer.controller;

import com.fairycompany.reviewer.controller.command.PagePath;
import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.RequestParameter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

@WebServlet("/upload_image")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 25)
public class ImageServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger();
    private static final String UPLOAD_DIR = "media";
    private static final String IMAGE = "image";

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
            try {
                Part part = request.getPart(IMAGE);
                processPart(request, response, part);
            } catch (IOException | ServletException e) {
                logger.log(Level.ERROR, "Failed to upload file.", e);
//                request.setAttribute(RequestAttribute.ERROR_KEY, BundleKey.INVALID_UPLOAD_FILE);
               request.getRequestDispatcher(PagePath.CREATE_USER).forward(request, response);
            }
        } else {
            logger.log(Level.WARN, "The user is trying to upload a file size that is too large");
//            session.setAttribute(RequestAttribute.ERROR_KEY, BundleKey.BIG_FILE_SIZE);
            response.sendRedirect(request.getContextPath() + PagePath.CREATE_USER_REDIRECT);
        }
    }

    private void processPart(HttpServletRequest request, HttpServletResponse response, Part part)
            throws ServletException, IOException {
        String fileName = part.getSubmittedFileName();
        if (!fileName.isBlank()) {
            String mimeType = getServletContext().getMimeType(fileName);
            if (mimeType.startsWith("image/")) {
                String uploadFileDirectory = request.getServletContext().getRealPath("");
                request.setAttribute(RequestAttribute.PART, part);
                request.setAttribute(RequestAttribute.IMAGE_UPLOAD_DIRECTORY, uploadFileDirectory);
                request.getRequestDispatcher("/controller").forward(request, response);
            } else {
                logger.log(Level.WARN, "The user is trying to upload a wrong file type");
//                HttpSession session = request.getSession();
//                session.setAttribute(RequestAttribute.ERROR_KEY, BundleKey.INVALID_UPLOAD_FILE_TYPE);
                response.sendRedirect(request.getContextPath() + PagePath.CREATE_USER_REDIRECT);
            }
        } else {                                                                            // todo это лишнее наверно
            request.getRequestDispatcher("/controller").forward(request, response);
        }
    }

//    private String defineUploadFileDirectory(HttpServletRequest request) {
//        String applicationDir = request.getServletContext().getRealPath("");
//        String uploadFileDirectory = applicationDir + UPLOAD_DIR + File.separator;
//        File fileSaveDir = new File(uploadFileDirectory);
//        if (!fileSaveDir.exists()) {
//            fileSaveDir.mkdirs();
//        }
//        return uploadFileDirectory;
//    }
}
