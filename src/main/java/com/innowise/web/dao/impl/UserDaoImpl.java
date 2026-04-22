package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.AbstractDao;
import com.innowise.web.dao.UserDao;
import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static com.innowise.web.config.PublicConstants.PASSWORD_PARAMETER;
import static com.innowise.web.config.PublicConstants.USER_NAME_SQL_PARAMETER;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {
  private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
  private static final String ADD_USER_SQL = "INSERT INTO users (user_name, password) VALUES (?, ?)";
  private static final String GET_USER_BY_NAME_SQL = "SELECT * FROM users WHERE user_name = ?";
  private static final String COUNT_USERS_BY_USER_NAME_SQL = "SELECT COUNT(user_name) FROM users WHERE user_name = ?";
  private static UserDaoImpl instance;

  private UserDaoImpl() {
  }

  public static UserDaoImpl getInstance() {
    if (instance == null) {
      instance = new UserDaoImpl();
    }
    return instance;
  }

  @Override
  public boolean add(User user) throws DaoException {
    logger.info("UserDaoImpl trying to add user: {}", user.getUserName());
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    boolean result = false;
    try (PreparedStatement statement = connection.prepareStatement(ADD_USER_SQL)) {
      statement.setString(1, user.getUserName());
      statement.setString(2, user.getPassword());
      statement.executeUpdate();
      result = true;
    } catch (SQLException e) {
      logger.error("Failed to add user {}: {}", user.toString(), e.getMessage()); // todo
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    logger.info("UserDaoImpl added user {}: {}", user.toString(), user.getUserName());
    return result;
  }

  @Override
  public boolean update(User user) throws DaoException {
    return false;
  }

  @Override
  public boolean delete(User user) throws DaoException {
    return false;
  }

  @Override
  public List<User> findAll() throws DaoException {
    return null;
  }

  @Override
  public Optional<User> findByUsername(String username) throws DaoException {
    logger.info("UserDaoImpl trying to find user by username: {}", username);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    Optional<User> userOptional = Optional.empty();
    try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_NAME_SQL)) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          logger.info("UserDaoImpl found user by username: {}", username);
          String userName = resultSet.getString(USER_NAME_SQL_PARAMETER);
          String password = resultSet.getString(PASSWORD_PARAMETER);
          User user = new User(userName, password);
          userOptional = Optional.of(user);
        }
      }
    } catch (SQLException e) {
      logger.error("Failed to find user by username: {}", e.getMessage());
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return userOptional;
  }

  @Override
  public boolean existsByUsername(String username) throws DaoException {
    logger.info("UserDaoImpl trying to check is user exist by username: {}", username);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    boolean exists = false;
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(COUNT_USERS_BY_USER_NAME_SQL)) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery();) {
        if (resultSet.next()) {
          int usersCount = resultSet.getInt(1);
          if (usersCount > 0) {
            logger.info("UserDaoImpl user {} exists.", username);
            exists = true;
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Failed to check user exists by username: {}", e.getMessage());
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return exists;
  }
}