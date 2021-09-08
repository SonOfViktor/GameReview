package com.fairycompany.reviewer.model.pool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import static com.fairycompany.reviewer.model.pool.DatabasePropertyReader.CONNECTION_POOL_SIZE;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static final AtomicBoolean isCreated = new AtomicBoolean(false);
    private static ConnectionPool instance;
    private static ReentrantLock locker = new ReentrantLock(true);
    private BlockingQueue<ProxyConnection> freeConnections;
    private BlockingQueue<ProxyConnection> busyConnection;
    private boolean destroyingPool;

    // todo add TimerTask

    private ConnectionPool() {
        freeConnections = new LinkedBlockingQueue<>(CONNECTION_POOL_SIZE);
        busyConnection = new LinkedBlockingQueue<>(CONNECTION_POOL_SIZE);
        Connection connection;

        for (int i = 0; i < CONNECTION_POOL_SIZE; i++) {
            try {
                connection = ConnectionFactory.createConnection();
                boolean isAdded = freeConnections.add(new ProxyConnection(connection));
                logger.log(Level.DEBUG, "Is connection added: {}", isAdded);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Connection wasn't created because database access error {}", e.getMessage());
            }
        }

        if (freeConnections.isEmpty()) {
            logger.log(Level.FATAL, "No one connection was created");
            throw new RuntimeException("No one connection was created");
        } else if (freeConnections.size() < CONNECTION_POOL_SIZE) {
            logger.log(Level.WARN, "Connection pool is not full");
            for (int i = freeConnections.size(); i < CONNECTION_POOL_SIZE; i++) {
                try {
                    connection = ConnectionFactory.createConnection();
                    freeConnections.add(new ProxyConnection(connection));
                } catch (SQLException e) {
                    logger.log(Level.FATAL, "Connection wasn't added. {}", e.getMessage());
                    throw new RuntimeException("Connection pool can't be full", e);
                }
            }
        }

        logger.log(Level.INFO, "Connection is successfully created");
    }

    public static ConnectionPool getInstance() {
        if (!isCreated.get()) {
            locker.lock();
            try {
                if (!isCreated.get()) {
                    instance = new ConnectionPool();
                    isCreated.set(true);
                }
            } finally {
                locker.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            busyConnection.put(connection);
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, "Current thread was interrupted {}", e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        boolean result = false;
        if (connection instanceof ProxyConnection && !destroyingPool && busyConnection.remove(connection)) {
            try {
                freeConnections.put((ProxyConnection) connection);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Current thread was interrupted {}", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
            result = true;
        } else {
            logger.log(Level.WARN, "Given connection isn't ProxyConnection or it can't be removed");
        }
        return result;
    }

    public void destroyPool() {
        destroyingPool = true;
        closeConnection(freeConnections);      // todo what todo if connections aren't closed?
        closeConnection(busyConnection);
        deregisterDriver();
    }

    private void closeConnection(BlockingQueue<ProxyConnection> connections) {
        while (!connections.isEmpty()) {
            try {
                connections.take().reallyClose();
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Connection isn't close due to database access error. {}", e.getMessage(), e);
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Connection isn't close due to current thread was interrupted {}", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private void deregisterDriver() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.log(Level.ERROR, "Driver isn't deregistered due to database access error. {}", e.getMessage(), e);
            }
        });
    }

}

