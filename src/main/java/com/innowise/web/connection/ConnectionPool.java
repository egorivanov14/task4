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
        logger.error("Failed to initialize database connection #{}", i, e);
      }
    }
    if (freeConnections.isEmpty()) {
      logger.fatal("Connection pool initialization failed: zero connections available");
      throw new ExceptionInInitializerError();
    } else if (freeConnections.size() < CONNECTION_POOL_CAPACITY) {
      logger.warn("Connection pool partially initialized. Attempting recovery...");
      tryToFixConnectionPool();
    }
    logger.info("Connection pool initialized with {} connections", freeConnections.size());
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
      logger.debug("Connection acquired. Active: {}, Free: {}", usingConnections.size(), freeConnections.size());
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
        logger.debug("Connection released. Active: {}, Free: {}", usingConnections.size(), freeConnections.size());
      }
    } catch (InterruptedException e) {
      logger.error("Failed to return connection to pool", e);
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
        logger.error("Failed to recover database connection during pool fix", e);
        throw new ExceptionInInitializerError(e);
      }
    }
    logger.info("Connection pool successfully recovered to {} connections", freeConnections.size());
  }

  public void destroyPool() {
    logger.info("Shutting down connection pool...");
    try {
      for (int i = 0; i < CONNECTION_POOL_CAPACITY; i++) {
        Connection connection = freeConnections.poll();
        if (connection != null) {
          connection.close();
        }
      }
      logger.info("All connections closed successfully");
    } catch (SQLException e) {
      logger.error("Failed to close one or more connections during pool destruction", e); //todo
    }
  }

  public void deregisterDriver() {
    try {
      java.sql.Driver driver = DriverManager.getDriver(URL);
      DriverManager.deregisterDriver(driver);
      logger.info("JDBC driver deregistered");
    } catch (SQLException e) {
      logger.error("Failed to deregister JDBC driver", e);//todo
    }
  }
}