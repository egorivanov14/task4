package com.innowise.web.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.innowise.web.config.PublicConstants.PASSWORD_PARAMETER;
import static com.innowise.web.config.PublicConstants.USER_PARAMETER;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/users"; //todo create new DB
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";
    private static final int CONNECTION_POOL_CAPACITY = 10;
    private static ConnectionPool instance;
    private static final Lock lock = new ReentrantLock();
    private BlockingQueue<Connection> connectionPool;

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            logger.error("Failed to register db driver: {}",  e.getMessage());
            throw new RuntimeException(e); //todo
        }
    }

    private ConnectionPool() {
        connectionPool = new LinkedBlockingQueue<>(CONNECTION_POOL_CAPACITY);
        Properties prop = new Properties();
        prop.put(USER_PARAMETER, USER);
        prop.put(PASSWORD_PARAMETER, PASSWORD);
        for (int i = 0; i < CONNECTION_POOL_CAPACITY; i++) {
            try {
                Connection connection = DriverManager.getConnection(URL, prop);
                connectionPool.add(connection);
            } catch (SQLException e) {
                logger.error("Failed to connect to data base: {}", e.getMessage());
            }
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public Optional<Connection> getConnection() {
        Optional<Connection> connection = Optional.empty();
        try {
            connection = Optional.of(connectionPool.take());
        } catch (InterruptedException e) {
            logger.error("Failed to get connection from pool: {}", e.getMessage()); //todo
        }
        return connection;
    }

    public boolean releaseConnection(Connection connection) {
        boolean result = false;
        try {
            connectionPool.put(connection); //todo check connection
            result = true;
        } catch (InterruptedException e) {
            logger.error("Failed to put connection to pool: {}", e.getMessage()); //todo
        }
        return result;
    }
}