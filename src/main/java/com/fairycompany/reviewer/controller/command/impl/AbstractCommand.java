package com.fairycompany.reviewer.controller.command.impl;

import com.fairycompany.reviewer.controller.command.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Class provided additional method for command implementation.
 */
public abstract class AbstractCommand implements Command {
    protected static final Logger logger = LogManager.getLogger();
    private static final String CONTENT_TYPE = "content-type";
    private static final String IMAGE_TYPE = "image/";
    protected SessionRequestContent content = new SessionRequestContent();
    protected Router router;

    /**
     * Extract values from request to content and initialize router
     *
     * @param request http request
     */
    protected void initCommand(HttpServletRequest request) {
        content.extractValues(request);

        String currentPage = (String) content.getSessionAttribute(SessionAttribute.CURRENT_PAGE);
        logger.log(Level.DEBUG, "Current page is " + currentPage);

        router = new Router(currentPage);
        router.setType(Router.RouterType.REDIRECT);
    }

    /**
     * Extract image input stream from part of request.
     *
     * @param request http request
     * @return true if input stream was extracted
     */
    protected boolean extractImageInputStream(HttpServletRequest request) {
        boolean isExtracted = true;
        Optional<InputStream> image = Optional.empty();

        try {
            Part part = request.getPart(RequestParameter.IMAGE);
            if (part.getSize() != 0) {
                String mimeType = part.getHeader(CONTENT_TYPE);
                if (mimeType.startsWith(IMAGE_TYPE)) {
                    image = Optional.of(part.getInputStream());
                    String partFileName = part.getSubmittedFileName();
                    String fileExtension = partFileName.substring(partFileName.lastIndexOf("."));
                    content.addRequestAttribute(RequestAttribute.FILE_EXTENSION, fileExtension);
                } else {
                    logger.log(Level.WARN, "The user tried to upload a file with a wrong type");
                    content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.INVALID_FILE_TYPE);
                    isExtracted = false;
                }
            }
            content.addRequestAttribute(RequestAttribute.IMAGE_INPUT_STREAM, image);
        } catch (IOException | ServletException e) {
            logger.log(Level.ERROR, "Failed to extract .", e);
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.UPLOAD_FILE_FAILED);
        }

        return isExtracted;
    }

    /**
     * Update image.
     *
     * @param request   http request
     * @param imagePath the image path
     * @return true if image updated
     */
    protected boolean updateImage(HttpServletRequest request, String imagePath) {
        boolean isFileUpdated = false;

        try {
            Part part = request.getPart(RequestParameter.IMAGE);

            String mimeType = part.getHeader(CONTENT_TYPE);
            if (mimeType.startsWith(IMAGE_TYPE)) {
                part.write(imagePath);
                isFileUpdated = true;
                logger.log(Level.DEBUG, "Photo {} updated", imagePath);
            } else {
                logger.log(Level.WARN, "The user tried to upload a file with a wrong type");
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.INVALID_FILE_TYPE);
            }
        } catch (IOException | ServletException e) {
            logger.log(Level.ERROR, "Failed to upload file.", e);
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.UPLOAD_FILE_FAILED);
        }

        return isFileUpdated;
    }
}
