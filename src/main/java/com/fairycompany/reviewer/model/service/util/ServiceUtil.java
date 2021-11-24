package com.fairycompany.reviewer.model.service.util;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.fairycompany.reviewer.controller.command.RequestParameter.LOGIN;

public class ServiceUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_FILE_NAME = "default.jpg";
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final ServiceUtil instance = new ServiceUtil();

    private ServiceUtil() {
    }

    public static ServiceUtil getInstance() {
        return instance;
    }

    public LocalDate getDateFromString(String stringDate) {
        LocalDate date = LocalDate.of(1, 1,1);
        if (stringDate != null && !stringDate.isEmpty()) {
            date = LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_PATTERN));
        }
        logger.log(Level.DEBUG, "Date is {}", date);
        return date;
    }

    public String saveImage(String imageUploadDirectory, Part part, String relativePath, String newFileName) {
        String generatedFileName;

        String uploadFileDir = imageUploadDirectory + relativePath;

        try {
            String submittedFileName = part.getSubmittedFileName();             //todo this line can make NullPointerEx
            String fileExtension = submittedFileName.substring(submittedFileName.lastIndexOf("."));
            generatedFileName = newFileName + fileExtension;
            String imagePath = uploadFileDir + File.separator + generatedFileName;
            part.write(imagePath);
            logger.log(Level.DEBUG, "Image path is {}", imagePath);
        } catch (IOException | NullPointerException e) {                    // todo ask may I do this
            logger.log(Level.ERROR, "Failed to upload file.", e);
            generatedFileName = DEFAULT_FILE_NAME;
        }
        return relativePath + generatedFileName;
    }

}
