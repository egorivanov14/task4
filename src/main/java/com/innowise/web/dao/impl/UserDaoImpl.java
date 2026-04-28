package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.AbstractDao;
import com.innowise.web.dao.UserDao;
import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;
import com.innowise.web.service.Role;
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
  private static final String ADD_USER_SQL = "INSERT INTO users (user_name, password, role_id) VALUES (?, ?, ?)";
  private static final String GET_USER_BY_NAME_SQL = "SELECT u.id, u.user_name, u.password, r.role_name FROM users u INNER JOIN roles r ON u.role_id = r.id WHERE user_name = ?";
  private static final String COUNT_USERS_BY_USER_NAME_SQL = "SELECT COUNT(user_name) FROM users WHERE user_name = ?";
  private static final String UPDATE_USER_ROLE_SQL = "UPDATE users SET role_id = ? WHERE user_name = ?";
  private static final String GET_ALL_USERS_SQL = "SELECT u.id, u.user_name, u.password, r.role_name FROM users u INNER JOIN roles r ON u.role_id = r.id";
  private static final String DELETE_USER_BY_USER_NAME_SQL = "DELETE FROM users WHERE user_name = ?";
  private static final String UPDATE_USER_NAME_SQL = "UPDATE users SET user_name = ? WHERE id = ?";
  private static final String GET_USER_BY_ID_SQL = "SELECT u.id, u.user_name, u.password, r.role_name FROM users u INNER JOIN roles r ON u.role_id = r.id WHERE u.id = ?";
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
      Role role = user.getRole();
      statement.setInt(3, role.getRoleId());
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
    logger.info("UserDaoImpl getting all users");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    List<User> users = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_USERS_SQL);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        User user = createUserFromResultSetParameters(resultSet);
        users.add(user);
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return users;
  }

  @Override
  public Optional<User> findById(Long id) throws DaoException {
    logger.info("UserDaoImpl getting user by id: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    Optional<User> userOptional = Optional.empty();
    try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_ID_SQL)) {
      statement.setLong(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        logger.info("UserDaoImpl found user by id: {}", id);
        User user = createUserFromResultSetParameters(resultSet);
        userOptional = Optional.of(user);
      }
    } catch (SQLException e) {
      logger.error("Failed to find user by id: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return userOptional;
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
          User user = createUserFromResultSetParameters(resultSet);
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
    Connection connection = connectionPool.getConnection();
    boolean exists = false;
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

  @Override
  public boolean updateRole(String username, int roleId) throws DaoException {
    logger.info("UserDaoImpl trying to update role for user: {}", username);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_ROLE_SQL)) {
      statement.setInt(1, roleId);
      statement.setString(2, username);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      logger.error("Failed to update role for user: {}", e.getMessage());
      throw new RuntimeException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean deleteUserByUsername(String username) throws DaoException {
    logger.info("UserDaoImpl trying to delete user by username: {}", username);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(DELETE_USER_BY_USER_NAME_SQL)) {
      statement.setString(1, username);
      int result = statement.executeUpdate();
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to delete user by username: {}", e.getMessage());
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean updateUsername(Long id, String newUsername) throws DaoException {
    logger.info("UserDaoImpl trying to update username by user id: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    boolean result = false;
    try (PreparedStatement statement = connection.prepareStatement(UPDATE_USER_NAME_SQL)) {
      Optional<User> userOptional = findById(id);
      if (userOptional.isPresent()) {
        User user = userOptional.get();
        statement.setString(1, newUsername);
        statement.setLong(2, user.getId());
        int sqlResult = statement.executeUpdate();
        result = sqlResult > 0;
      }
    } catch (SQLException e) {
      logger.error("Failed to update username by user id: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return result;
  }

  private User createUserFromResultSetParameters(ResultSet resultSet) throws SQLException {
    Long id = resultSet.getLong(ID_PARAMETER);
    String userName = resultSet.getString(USERNAME_SQL_PARAMETER);
    String password = resultSet.getString(PASSWORD_PARAMETER);
    String roleName = resultSet.getString(ROLE_NAME_PARAMETER);
    Role role = Role.valueOf(roleName);
    return new User(id, userName, password, role);
  }
}