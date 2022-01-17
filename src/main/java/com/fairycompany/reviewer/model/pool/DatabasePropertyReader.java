package com.fairycompany.reviewer.model.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class DatabasePropertyReader {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATABASE_PROPERTY_FILE = "properties\\database";
    private static final String DATABASE_DRIVER_KEY = "db.driver";
    private static final String DATABASE_USERNAME_KEY = "user";
    private static final String DATABASE_PASSWORD_KEY = "password";
    private static final String CONNECTION_POOL_SIZE_KEY = "poolsize";
    private static final String DATABASE_URL_KEY = "db.url";
    static final String DATABASE_DRIVER;
    static final String DATABASE_USERNAME;
    static final String DATABASE_PASSWORD;
    static final int CONNECTION_POOL_SIZE;
    static final String DATABASE_URL;

    static {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_PROPERTY_FILE);
            DATABASE_DRIVER = resourceBundle.getString(DATABASE_DRIVER_KEY);
            DATABASE_USERNAME = resourceBundle.getString(DATABASE_USERNAME_KEY);
            DATABASE_PASSWORD = resourceBundle.getString(DATABASE_PASSWORD_KEY);
            CONNECTION_POOL_SIZE = Integer.parseInt(resourceBundle.getString(CONNECTION_POOL_SIZE_KEY));
            DATABASE_URL = resourceBundle.getString(DATABASE_URL_KEY);
        } catch (MissingResourceException e) {
            logger.log(Level.FATAL, "File {} or key {} is not found. {}",
                    DATABASE_PROPERTY_FILE, e.getKey(), e.getMessage());
            throw new RuntimeException("File or key is not found", e);
        }
    }
}