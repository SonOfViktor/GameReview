package com.fairycompany.reviewer.model.util;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.validator.CommonValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.fairycompany.reviewer.controller.command.RequestParameter.ACTUAL_PAGE;

public class ServiceUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final LocalDate DEFAULT_DATE = LocalDate.of(1, 1, 1);
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
    private static final long FIRST_PAGE = 1;
    private static final ServiceUtil instance = new ServiceUtil();

    private ServiceUtil() {
    }

    public static ServiceUtil getInstance() {
        return instance;
    }

    public LocalDate getDateFromString(String stringDate) {
        return (stringDate != null && stringDate.matches(DATE_PATTERN)) ?
                LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)) :
                DEFAULT_DATE;
    }

    public String saveImage(String uploadDirectory, String relativeImagePath, String fileExtension, InputStream imageStream) throws DaoException{
        String imagePathString = uploadDirectory + relativeImagePath + fileExtension;

        try {
            Path imagePath = new File(imagePathString).toPath();
            logger.log(Level.DEBUG, "Image path is {}", imagePath);
            Files.copy(imageStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Failed to upload file {}", imagePathString, e);
            throw new DaoException("Failed to upload file " + imagePathString);
        }

        return relativeImagePath + fileExtension;
    }

    public String saveDefaultImage(String uploadDirectory, String relativeImagePath, String defaultFile) throws DaoException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(defaultFile).getFile());
        Path sourceFile = file.toPath();

        String fileExtension = defaultFile.substring(defaultFile.lastIndexOf("."));
        String imagePath = uploadDirectory + relativeImagePath + fileExtension;
        Path destFile = Paths.get(imagePath);

        try {
            Files.copy(sourceFile, destFile);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Failed to upload default file {}", imagePath, e);
            throw new DaoException("Failed to upload file " + imagePath);
        }

        return relativeImagePath + fileExtension;
    }

    public void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            logger.log(Level.DEBUG, "File {} deleted", path);
        }
    }

    public void makeDir(String uploadFileDir) {
        File fileSaveDir = new File(uploadFileDir);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
    }

    public long takeActualPage(SessionRequestContent content) {
        CommonValidator commonValidator = CommonValidator.getInstance();
        String actualPageString = content.getRequestParameter(ACTUAL_PAGE);

        return commonValidator.isStringLong(actualPageString) ?
                Long.parseLong(content.getRequestParameter(ACTUAL_PAGE)) : FIRST_PAGE;
    }

}
