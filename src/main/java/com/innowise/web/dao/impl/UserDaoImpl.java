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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.innowise.web.config.PublicConstants.*;

public class UserDaoImpl extends AbstractDao<User> implements UserDao {
  private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
  private static UserDaoImpl instance;
  private static final String ADD_USER_SQL = "INSERT INTO users (user_name, password, role_id) VALUES (?, ?, ?)";
  private static final String GET_USER_BY_NAME_SQL = "SELECT id, user_name, password, role_id FROM users WHERE user_name = ?";
  private static final String COUNT_USERS_BY_USER_NAME_SQL = "SELECT COUNT(user_name) FROM users WHERE user_name = ?";
  private static final String UPDATE_USER_ROLE_SQL = "UPDATE users SET role_id = ? WHERE user_name = ?";
  private static final String GET_ALL_USERS_SQL = "SELECT id, user_name, password, role_id FROM users";
  private static final String DELETE_USER_BY_USER_NAME_SQL = "DELETE FROM users WHERE user_name = ?";
  private static final String UPDATE_USER_NAME_SQL = "UPDATE users SET user_name = ? WHERE id = ?";
  private static final String GET_USER_BY_ID_SQL = "SELECT id, user_name, password, role_id FROM users WHERE id = ?";
  private static final String DELETE_USER_BY_ID_SQL = "DELETE FROM users WHERE id = ?";
  private static final String EXISTS_BY_ID_SQL = "SELECT EXISTS (SELECT 1 FROM users WHERE id = ?)";
  private static final String EXISTS_BY_USERNAME_SQL = "SELECT EXISTS (SELECT 1 FROM users WHERE user_name = ?)";

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
    logger.debug("Attempting to register user: {}", user.getUserName());
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(ADD_USER_SQL)) {
      statement.setString(1, user.getUserName());
      statement.setString(2, user.getPassword());
      int roleId = user.getRoleId();
      statement.setInt(3, roleId);
      int resultSet = statement.executeUpdate();
      logger.info("Successfully registered user: {}", user.getUserName());
      return resultSet > 0;
    } catch (SQLException e) {
      logger.error("Failed to register user '{}'", user.getUserName(), e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean update(User user) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean delete(User user) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean deleteById(Long id) throws DaoException {
    logger.debug("Deleting user by ID: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_ID_SQL)) {
      statement.setLong(1, id);
      int resultSet = statement.executeUpdate();
      return resultSet > 0;
    } catch (SQLException e) {
      logger.error("Failed to deleteById user with ID: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<User> findAll() throws DaoException {
    logger.debug("Fetching all users");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_USERS_SQL);
         ResultSet resultSet = statement.executeQuery()) {
      List<User> users = new ArrayList<>();
      while (resultSet.next()) {
        User user = createUserFromResultSetParameters(resultSet);
        users.add(user);
      }
      return users;
    } catch (SQLException e) {
      logger.error("Failed to fetch all users", e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public Optional<User> findById(Long id) throws DaoException {
    logger.debug("Fetching user by ID: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_SQL)) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        Optional<User> userOptional = Optional.empty();
        if (resultSet.next()) {
          logger.debug("User found by ID: {}", id);
          User user = createUserFromResultSetParameters(resultSet);
          userOptional = Optional.of(user);
        }
        return userOptional;
      }
    } catch (SQLException e) {
      logger.error("Failed to fetch user by ID: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean existsById(Long id) throws DaoException {
    logger.debug("Checking existence for user ID: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(EXISTS_BY_ID_SQL)) {
      statement.setLong(1, id);
      ResultSet resultSet = statement.executeQuery();
      boolean exists = false;
      if (resultSet.next()) {
        exists = resultSet.getBoolean(1);
      }
      logger.debug("User ID: {} exists: {}", id, exists);
      return exists;
    } catch (SQLException e) {
      logger.error("Database error while checking existence for user ID: {}", id, e);
      throw new DaoException(e);
    }finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public Optional<User> findByUsername(String username) throws DaoException {
    logger.debug("Fetching user by username: {}", username);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_NAME_SQL)) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        Optional<User> userOptional = Optional.empty();
        if (resultSet.next()) {
          logger.debug("User found by username: {}", username);
          User user = createUserFromResultSetParameters(resultSet);
          userOptional = Optional.of(user);
        }
        return userOptional;
      }
    } catch (SQLException e) {
      logger.error("Failed to fetch user by username: '{}'", username, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean existsByUsername(String username) throws DaoException {
    logger.debug("Checking existence of user: {}", username);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(EXISTS_BY_USERNAME_SQL)) {
      statement.setString(1, username);
      try (ResultSet resultSet = statement.executeQuery()) {
        boolean exists = false;
        if (resultSet.next()) {
          exists = resultSet.getBoolean(1);
        }
        logger.debug("User '{}' exists: {}", username, exists);
        return exists;
      }
    } catch (SQLException e) {
      logger.error("Failed to check user existence for username: '{}'", username, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean updateRole(String username, int roleId) throws DaoException {
    logger.debug("Updating role for user '{}' to ID: {}", username, roleId);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_ROLE_SQL)) {
      statement.setInt(1, roleId);
      statement.setString(2, username);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      logger.error("Failed to update role for user: '{}'", username, e);
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean deleteUserByUsername(String username) throws DaoException {
    logger.debug("Deleting user by username: {}", username);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_USER_NAME_SQL)) {
      statement.setString(1, username);
      int result = statement.executeUpdate();
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to deleteById user by username: '{}'", username, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean updateUsername(Long id, String newUsername) throws DaoException {
    logger.debug("Updating username for user ID: {} to '{}'", id, newUsername);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_NAME_SQL)) {
      Optional<User> userOptional = findById(id);
      boolean result = false;
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        statement.setString(1, newUsername);
        statement.setLong(2, user.getId());
        int sqlResult = statement.executeUpdate();
        result = sqlResult > 0;
      }
      return result;
    } catch (SQLException e) {
      logger.error("Failed to update username for user ID: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private User createUserFromResultSetParameters(ResultSet resultSet) throws SQLException {
    Long id = resultSet.getLong(ID_PARAMETER);
    String userName = resultSet.getString(USERNAME_SQL_PARAMETER);
    String password = resultSet.getString(PASSWORD_PARAMETER);
    int roleId = resultSet.getInt(ROLE_ID_PARAMETER);
    return new User(id, userName, password, roleId);
  }
}