package com.fairycompany.reviewer.model.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.fairycompany.reviewer.model.pool.DatabasePropertyReader.DATABASE_DRIVER;
import static com.fairycompany.reviewer.model.pool.DatabasePropertyReader.DATABASE_URL;
import static com.fairycompany.reviewer.model.pool.DatabasePropertyReader.DATABASE_USERNAME;
import static com.fairycompany.reviewer.model.pool.DatabasePropertyReader.DATABASE_PASSWORD;

class ConnectionFactory {
    private static final Logger logger = LogManager.getLogger();

    static {
        try {
            Class.forName(DATABASE_DRIVER);
        } catch (ClassNotFoundException e) {
            logger.log(Level.FATAL, "Driver class isn't found, it can't be registered {}", e.getMessage());
            throw new RuntimeException("Driver class isn't found", e);
        }
    }

    private ConnectionFactory() {}

    static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
    }
}
