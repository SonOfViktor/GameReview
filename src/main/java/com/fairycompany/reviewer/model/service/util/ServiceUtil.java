package com.fairycompany.reviewer.model.service.util;

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
    private static final LocalDate DEFAULT_DATE = LocalDate.of(0001, 01, 01);
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final int DATE_LENGTH = 10;
    private static final ServiceUtil instance = new ServiceUtil();

    private ServiceUtil() {
    }

    public static ServiceUtil getInstance() {
        return instance;
    }

    public LocalDate getDateFromString(String stringDate) {
        return (stringDate != null && stringDate.length() == DATE_LENGTH) ?      //todo проверить оформление
                LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_PATTERN)) :
                DEFAULT_DATE;
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

    public void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

}
