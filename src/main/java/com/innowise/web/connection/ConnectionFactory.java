package com.innowise.web.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static com.innowise.web.config.PublicConstants.PASSWORD_PARAMETER;
import static com.innowise.web.config.PublicConstants.USER_PARAMETER;

class ConnectionFactory {
  private static final Logger logger = LogManager.getLogger(ConnectionFactory.class);
  private static final String USER = "postgres";
  private static final String PASSWORD = "admin";
  static final String URL = "jdbc:postgresql://localhost:5432/users"; //todo create new DB
  private static ConnectionFactory instance;

  static {
    try {
      DriverManager.registerDriver(new Driver());
    } catch (SQLException e) {
      logger.fatal("Failed to register db driver: {}", e.getMessage());
      throw new ExceptionInInitializerError(e);
    }
  }

  private ConnectionFactory() {
  }

  static ConnectionFactory getInstance() {
    if (instance == null) {
      instance = new ConnectionFactory();
    }
    return instance;
  }

  Connection getConnection() throws SQLException {
    Properties properties = new Properties();
    properties.put(USER_PARAMETER, USER);
    properties.put(PASSWORD_PARAMETER, PASSWORD);
    return DriverManager.getConnection(URL, properties);
  }
}