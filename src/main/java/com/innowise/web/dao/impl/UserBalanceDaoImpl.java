package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.UserBalanceDao;
import com.innowise.web.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBalanceDaoImpl implements UserBalanceDao {
  private static final Logger LOG = LogManager.getLogger(UserBalanceDaoImpl.class);
  private static UserBalanceDaoImpl instance;
  private static final String ADD_EMPTY_BALANCE_SQL = "INSERT INTO user_balance (user_id, balance) VALUES (?, 0)";
  private static final String GET_BALANCE_BY_USER_ID_SQL = "SELECT balance FROM user_balance WHERE user_id = ?";
  private static final String ADD_QUANTITY_BY_USER_ID_SQL = "UPDATE user_balance SET balance = balance + ? WHERE user_id = ?";
  private static final String DEDUCT_BALANCE_BY_USER_ID_SQL = "UPDATE user_balance SET balance = balance - ? WHERE user_id = ? AND balance >= ?";

  private UserBalanceDaoImpl() {
  }

  public static UserBalanceDaoImpl getInstance() {
    if (instance == null) {
      instance = new UserBalanceDaoImpl();
    }
    return instance;
  }

  @Override
  public boolean addEmpty(Connection connection, Long userId) throws DaoException { // todo logs
    try (PreparedStatement statement = connection.prepareStatement(ADD_EMPTY_BALANCE_SQL)) {
      statement.setLong(1, userId);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }

  @Override
  public Long getBalanceByUserId(Long userId) throws DaoException { // todo logs
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_BALANCE_BY_USER_ID_SQL)) {
      statement.setLong(1, userId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getLong(1);
      } else {
        throw new DaoException("Balance not found");
      }
    } catch (SQLException e) {
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean updateBalanceByUserId(Long userId, Long amount) throws DaoException { // todo logs
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(ADD_QUANTITY_BY_USER_ID_SQL)) {
      statement.setLong(1, amount);
      statement.setLong(2, userId);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean deductBalance(Connection connection, Long userId, Long amount) throws DaoException { // todo logs
    try (PreparedStatement statement = connection.prepareStatement(DEDUCT_BALANCE_BY_USER_ID_SQL)) {
      statement.setLong(1, amount);
      statement.setLong(2, userId);
      statement.setLong(3, amount);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new DaoException(e);
    }
  }
}