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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

public abstract class AbstractCommand implements Command {
    protected static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_FILE_EXTENSION = ".jpg";
    private static final String CONTENT_TYPE = "content-type";
    private static final String IMAGE_TYPE = "image/";
    protected SessionRequestContent content = new SessionRequestContent();
    protected Router router;

    protected void initCommand(HttpServletRequest request) {
        content.extractValues(request);

        String currentPage = (String) content.getSessionAttribute(SessionAttribute.CURRENT_PAGE);
        logger.log(Level.DEBUG, "Current page is " + currentPage);

        router = new Router(currentPage);
        router.setType(Router.RouterType.REDIRECT);
    }

    protected Optional<String> saveImage(HttpServletRequest request, String uploadDirectory, String defaultFile) {
        Optional<String> imageName = Optional.empty();

        try {
            Part part = request.getPart(RequestParameter.IMAGE);
            if (isPartImage(part)) {
                String newFileName = UUID.randomUUID().toString();
                String generatedFileName = newFileName + DEFAULT_FILE_EXTENSION;

                File fileSaveDir = new File(uploadDirectory);
                if (!fileSaveDir.exists()) {
                    fileSaveDir.mkdirs();
                }

                String partFileName = part.getSubmittedFileName();
                if (!partFileName.isEmpty()) {
                    String fileExtension = partFileName.substring(partFileName.lastIndexOf("."));
                    generatedFileName = newFileName + fileExtension;
                    String imagePath = uploadDirectory + File.separator + generatedFileName;
                    part.write(imagePath);
                    logger.log(Level.DEBUG, "Image path is {}", imagePath);
                } else {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    File file = new File(classLoader.getResource(defaultFile).getFile());
                    Path sourceFile = file.toPath();
                    String imagePath = uploadDirectory + File.separator + generatedFileName;
                    Path destFile = Paths.get(imagePath);
                    Files.copy(sourceFile, destFile);
                }

                imageName = Optional.of(generatedFileName);
            } else {
                logger.log(Level.WARN, "The user tried to upload a file with a wrong type");
                content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.INVALID_FILE_TYPE);
            }
        } catch (IOException | ServletException e) {
            logger.log(Level.ERROR, "Failed to upload file.", e);
            content.addSessionAttribute(SessionAttribute.SESSION_MESSAGE_ERROR, LocaleMessageKey.UPLOAD_FILE_FAILED);
        }

        return imageName;
    }

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

    protected void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            logger.log(Level.DEBUG, "File {} deleted", path);
        }
    }

    private boolean isPartImage(Part part) {
        String mimeType = part.getHeader(CONTENT_TYPE);

        return mimeType.startsWith(IMAGE_TYPE) || part.getSize() == 0;
    }
}
