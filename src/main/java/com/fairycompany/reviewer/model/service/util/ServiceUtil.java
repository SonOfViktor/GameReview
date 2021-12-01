package com.fairycompany.reviewer.model.service.util;

import com.fairycompany.reviewer.controller.command.RequestAttribute;
import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.fairycompany.reviewer.controller.command.RequestParameter.LOGIN;

public class ServiceUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final String DEFAULT_FILE_EXTENSION = ".jpg";
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

    public String saveImage(String uploadDirectory, String relativePath, Part part, String newFileName, String defaultFile) {
        String generatedFileName = newFileName + DEFAULT_FILE_EXTENSION;

        String uploadFileDir = uploadDirectory + relativePath;

        File fileSaveDir = new File(uploadFileDir);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

        try {
            String partFileName = part.getSubmittedFileName();
            if (!partFileName.isBlank()) {
                String fileExtension = partFileName.substring(partFileName.lastIndexOf("."));
                generatedFileName = newFileName + fileExtension;
                String imagePath = uploadFileDir + generatedFileName;
                part.write(imagePath);
                logger.log(Level.DEBUG, "Image path is {}", imagePath);
            } else {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                File file = new File(classLoader.getResource(defaultFile).getFile());
                Path sourceFile = file.toPath();
                String imagePath = uploadFileDir + generatedFileName;
                Path destFile = Paths.get(imagePath);
                Files.copy(sourceFile, destFile);
            }
        } catch (IOException e) {
            logger.log(Level.ERROR, "Failed to upload file.", e);
        }

        return relativePath + generatedFileName;
    }

}
