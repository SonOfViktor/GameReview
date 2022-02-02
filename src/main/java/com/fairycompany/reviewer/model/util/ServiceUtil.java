package com.fairycompany.reviewer.model.util;

import com.fairycompany.reviewer.controller.command.SessionRequestContent;
import com.fairycompany.reviewer.exception.ServiceException;
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

import static com.fairycompany.reviewer.controller.command.RequestParameter.ACTUAL_PAGE;

/**
 * This class contains utility method for services.
 */
public class ServiceUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final LocalDate DEFAULT_DATE = LocalDate.of(1, 1, 1);
    private static final long FIRST_PAGE = 1;
    private static final ServiceUtil instance = new ServiceUtil();

    private ServiceUtil() {
    }

    /**
     * Gets instance of ServiceUtil.
     *
     * @return the instance of ServiceUtil
     */
    public static ServiceUtil getInstance() {
        return instance;
    }

    /**
     * Parse provided String to LocalDate
     *
     * @param stringDate the string date
     * @return parsed {@code LocalDate} date or default date 01-01-01 if string can't be parsed
     */
    public LocalDate getDateFromString(String stringDate) {
        CommonValidator commonValidator = CommonValidator.getInstance();

        return (commonValidator.isStringDate(stringDate)) ? LocalDate.parse(stringDate) : DEFAULT_DATE;
    }

    /**
     * Take actual page from request content and parse it to long.
     *
     * @param content session request content
     * @return parsed actual page or 1 if provided actual page can't be parsed to long
     */
    public long takeActualPage(SessionRequestContent content) {
        CommonValidator commonValidator = CommonValidator.getInstance();
        String actualPageString = content.getRequestParameter(ACTUAL_PAGE);

        return commonValidator.isStringLong(actualPageString) ?
                Long.parseLong(content.getRequestParameter(ACTUAL_PAGE)) : FIRST_PAGE;
    }

    /**
     * Save image to file.
     *
     * @param uploadDirectory   the upload directory
     * @param relativeImagePath the relative image path
     * @param fileExtension     the file extension
     * @param imageStream       the image stream that contains image bytes
     * @return relative image path
     * @throws ServiceException if IOException occurred when file copied
     */
    public String saveImage(String uploadDirectory, String relativeImagePath, String fileExtension, InputStream imageStream) throws ServiceException{
        String imagePathString = uploadDirectory + relativeImagePath + fileExtension;

        try {
            Path imagePath = new File(imagePathString).toPath();
            logger.log(Level.DEBUG, "Image path is {}", imagePath);
            Files.copy(imageStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Failed to upload file {}", imagePathString, e);
            throw new ServiceException("Failed to upload file " + imagePathString);
        }

        return relativeImagePath + fileExtension;
    }

    /**
     * Save default image to file.
     *
     * @param uploadDirectory   the upload directory
     * @param relativeImagePath the relative image path
     * @param defaultFile       address of default file
     * @return relative image path
     * @throws ServiceException if IOException occurred when file copied
     */
    public String saveDefaultImage(String uploadDirectory, String relativeImagePath, String defaultFile) throws ServiceException{
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
            throw new ServiceException("Failed to upload file " + imagePath);
        }

        return relativeImagePath + fileExtension;
    }

    /**
     * Delete file if it exists
     *
     * @param path absolute path of file
     */
    public void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
            logger.log(Level.DEBUG, "File {} deleted", path);
        }
    }

    /**
     * Make directory named by uploadFileDir
     *
     * @param uploadFileDir address of directory
     */
    public void makeDir(String uploadFileDir) {
        File fileSaveDir = new File(uploadFileDir);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }
    }
}
