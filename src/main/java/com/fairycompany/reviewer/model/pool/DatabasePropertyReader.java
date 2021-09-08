package com.fairycompany.reviewer.model.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class DatabasePropertyReader {
    private static final Logger logger = LogManager.getLogger();
    private static final String DATABASE_PROPERTY_FILE = "properties\\database";
    static final String DATABASE_DRIVER;
    static final String DATABASE_USERNAME;
    static final String DATABASE_PASSWORD;
    static final int CONNECTION_POOL_SIZE;
    static final String DATABASE_URL;

    static {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(DATABASE_PROPERTY_FILE);
            DATABASE_DRIVER = resourceBundle.getString("db.driver");
            DATABASE_USERNAME = resourceBundle.getString("user");
            DATABASE_PASSWORD = resourceBundle.getString("password");
            CONNECTION_POOL_SIZE = Integer.parseInt(resourceBundle.getString("poolsize"));
            DATABASE_URL = resourceBundle.getString("db.url");
        } catch (MissingResourceException e) {
            logger.log(Level.FATAL, "File {} or key {} is not found. {}",
                    DATABASE_PROPERTY_FILE, e.getKey(), e.getMessage());
            throw new RuntimeException("File or key is not found", e);
        }
    }
}