package com.innowise.web.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.innowise.web.connection.ConnectionFactory.URL;

public class ConnectionPool {
  private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
  private static final int CONNECTION_POOL_CAPACITY = 10;
  private static final Lock lock = new ReentrantLock();
  private static ConnectionPool instance;
  private final BlockingQueue<Connection> usingConnections;
  private final BlockingQueue<Connection> freeConnections;

  private ConnectionPool() {
    freeConnections = new LinkedBlockingQueue<>(CONNECTION_POOL_CAPACITY);
    usingConnections = new LinkedBlockingQueue<>(CONNECTION_POOL_CAPACITY);
    ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
    for (int i = 0; i < CONNECTION_POOL_CAPACITY; i++) {
      try {
        Connection connection = connectionFactory.getConnection();
        freeConnections.add(connection);
      } catch (SQLException e) {
        logger.error("Failed to connect to data base: {}", e.getMessage());
      }
    }
    if (freeConnections.isEmpty()) {
      logger.fatal("Something wrong with db. Impossible to make connection to db.");
      throw new ExceptionInInitializerError();
    } else if (freeConnections.size() < CONNECTION_POOL_CAPACITY) {
      logger.error("Connection pool is not full. Try to fix pool.");
      tryToFixConnectionPool();
    }
    logger.info("Connection pool created.");
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

  public Connection getConnection() {
    Connection connection = null;
    try {
      connection = freeConnections.take();
      usingConnections.put(connection);
    } catch (InterruptedException e) {
      logger.error("Failed to get connection from pool: {}", e.getMessage());
      Thread.currentThread().interrupt();
    }
    return connection;
  }

  public void releaseConnection(Connection connection) {
    try {
      if(usingConnections.remove(connection)) {
        freeConnections.put(connection); // todo. do something with wrong connection
      }
    } catch (InterruptedException e) {
      logger.error("Failed to put connection {} to pool: {}", connection.toString(), e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

  private void tryToFixConnectionPool() {
    int brokeConnections = CONNECTION_POOL_CAPACITY - freeConnections.size();
    ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
    for (int i = 0; i < brokeConnections; i++) {
      try {
        Connection connection = connectionFactory.getConnection();
        freeConnections.add(connection);
      } catch (SQLException e) {
        logger.error("Failed to fix connection to data base: {}", e.getMessage());
        throw new ExceptionInInitializerError(e);
      }
    }
    logger.info("Connection pool fixed.");
  }

  public void destroyPool() {
    try {
      for (int i = 0; i < CONNECTION_POOL_CAPACITY; i++) {
        Connection connection = freeConnections.poll();
        if (connection != null) {
          connection.close();
        }
      }
    } catch (SQLException e) {
      logger.error("Failed to destroy pool: {}", e.getMessage()); //todo
    }
    logger.info("Connection pool destroyed.");
  }

  public void deregisterDriver() {
    try {
      java.sql.Driver driver = DriverManager.getDriver(URL); // todo search how to deregister correctly
      DriverManager.deregisterDriver(driver);
    } catch (SQLException e) {
      logger.error("Failed to deregister driver: {}", e.getMessage());// todo
    }
    logger.info("Connection pool driver deregistered.");
  }
}