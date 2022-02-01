package com.fairycompany.reviewer.model.dao;

import com.fairycompany.reviewer.exception.DaoException;
import com.fairycompany.reviewer.model.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class that manages transactions.
 */
public class TransactionManager {
    private static final Logger logger = LogManager.getLogger();
    private static final TransactionManager instance = new TransactionManager();
    private ThreadLocal<Connection> threadConnection = new ThreadLocal<>();

    private TransactionManager() {
    }

    /**
     * Gets instance of transaction manager.
     *
     * @return the instance of transaction manager
     */
    public static TransactionManager getInstance() {
        return instance;
    }

    /**
     * Gets connection from connection pool and associates it with current thread
     *
     * @throws DaoException if SQL exception occurred
     */
    public void initTransaction() throws DaoException {
        if (threadConnection.get() == null) {
            try {
                Connection connection = ConnectionPool.getInstance().getConnection();
                if (connection == null) {
                    throw new DaoException("This thread was interrupted and connection is null");
                }
                threadConnection.set(connection);
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to change disable autocommit. A database access error occurs", e);
                throw new DaoException("Failed to change disable autocommit. A database access error occurs", e);
            }
        }
    }

    /**
     * Gets connection for this thread.
     *
     * @return connection
     * @throws DaoException if transaction wasn't initialized.
     */
    public Connection getConnection() throws DaoException {
        Connection connection = threadConnection.get();
        if (connection != null) {
            return connection;
        } else {
            throw new DaoException("Failed to get connection. The transaction wasn't initialized.");
        }
    }


    /**
     * Release connection for this thread
     */
    public void endTransaction() {
        Connection connection = threadConnection.get();
        if (connection != null) {
            try {
                threadConnection.remove();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to set commit true. A database access error occurs: ", e);
            } finally {
                close(connection);
            }
        }
    }


    /**
     * Commit transaction.
     *
     * @throws DaoException if SQL exception occurred
     */
    public void commit() throws DaoException {
        Connection connection = threadConnection.get();
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Failed to commit. A database access error occurs: ", e);
                throw new DaoException("Failed to commit. A database access error occurs: ", e);
            }
        }
    }

    /**
     * Rollback transaction.
     */
    public void rollback() {
        Connection connection = threadConnection.get();
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Failed to rollback. A database access error occurs: ", e);
        }
    }

    private void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.log(Level.ERROR, "Connection didn't close. ", e);
        }
    }
}
