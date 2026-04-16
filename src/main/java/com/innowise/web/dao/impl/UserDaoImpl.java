package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.DaoAbstract;
import com.innowise.web.dao.UserDao;
import com.innowise.web.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserDaoImpl extends DaoAbstract<User> implements UserDao {
    private static final Logger logger = LogManager.getLogger(UserDaoImpl.class);
    private static final String ADD_USER_SQL = "INSERT INTO users (user_name, password) VALUES (?, ?)";
    private static final String GET_USER_BY_NAME_SQL = "SELECT * FROM users WHERE user_name = ?";
    private static final String COUNT_USERS_BY_USER_NAME_SQL = "SELECT COUNT(user_name) FROM users WHERE user_name = ?";
    private static UserDaoImpl instance;
    private static final Lock lock = new ReentrantLock();

    private UserDaoImpl() {
    }

    public static UserDaoImpl getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new UserDaoImpl();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    @Override
    public boolean add(User user) {
        logger.info("UserDaoImpl trying to add user: {}", user.getUserName());
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Optional<Connection> connectionOptional = connectionPool.getConnection();
        boolean result = false;
        if (connectionOptional.isPresent()) {
            Connection connection = connectionOptional.get();
            try (PreparedStatement statement = connection.prepareStatement(ADD_USER_SQL)) {
                statement.setString(1, user.getUserName());
                statement.setString(2, user.getPassword());
                statement.executeUpdate();
                result = true;
            } catch (SQLException e) {
                logger.error("Failed to add user: {}", e.getMessage()); // todo
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }
        logger.info("UserDaoImpl added user: {}", user.getUserName());
        return result;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        logger.info("UserDaoImpl trying to find user by username: {}", username);
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Optional<Connection> connectionOptional = connectionPool.getConnection();
        Optional<User> userOptional = Optional.empty();
        if (connectionOptional.isPresent()) {
            Connection connection = connectionOptional.get();
            try (PreparedStatement statement = connection.prepareStatement(GET_USER_BY_NAME_SQL)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        logger.info("UserDaoImpl found user by username: {}", username);
                        String userName = resultSet.getString("user_name");
                        String password = resultSet.getString("password");
                        User user = new User(userName, password);
                        userOptional = Optional.of(user);
                    }
                }
            } catch (SQLException e) {
                logger.error("Failed to find user by username: {}", e.getMessage());
                return Optional.empty();
            } finally {
                connectionPool.releaseConnection(connection);
            }
        }
        return userOptional;
    }

    @Override
    public boolean existsByUsername(String username) {
        logger.info("UserDaoImpl trying to check is user exist by username: {}", username);
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Optional<Connection> connectionOptional = connectionPool.getConnection();
        boolean exists = false;
        if(connectionOptional.isPresent()) {
            Connection connection = connectionOptional.get();
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
                logger.error("Failed to check user exists by username: {}", e.getMessage());// todo
            }finally {
                connectionPool.releaseConnection(connection);
            }
        }
        return exists;
    }
}