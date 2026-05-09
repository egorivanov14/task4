package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.AbstractDao;
import com.innowise.web.dao.ShoppingCartDao;
import com.innowise.web.dto.ShoppingCartItemDto;
import com.innowise.web.entity.ShoppingCartItem;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
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

public class ShoppingCartDaoImpl extends AbstractDao<ShoppingCartItem> implements ShoppingCartDao {
  private static final Logger logger = LogManager.getLogger(ShoppingCartDaoImpl.class);
  private static ShoppingCartDaoImpl instance;
  private static final String INSERT_ITEM_INTO_SHOPPING_CART_SQL = "INSERT INTO shopping_cart (user_id, good_id, quantity) VALUES (?, ?, ?)";
  private static final String UPDATE_SHOPPING_CART_ITEM_QUANTITY_BY_ID_SQL = "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id = ? AND good_id = ?";
  private static final String GET_SHOPPING_CART_ITEM_BY_ID_SQL = "SELECT user_id, good_id, quantity FROM shopping_cart WHERE user_id = ? AND good_id = ?";
  private static final String EXISTS_SQL = "SELECT EXISTS(SELECT 1 FROM shopping_cart WHERE user_id = ? AND good_id = ?)";
  private static final String GET_ALL_SHOPPING_CART_ITEMS_BY_USER_ID_WITH_GOOD_NAME_SQL = "SELECT c.good_id, g.name, g.price, c.quantity FROM shopping_cart c INNER JOIN goods g ON c.good_id = g.id WHERE c.user_id = ?";
  private static final String DELETE_SHOPPING_CART_ITEM_BY_ID_SQL = "DELETE FROM shopping_cart WHERE user_id = ? AND good_id = ?";
  private static final String DECREMENT_QUANTITY_SQL = "UPDATE shopping_cart SET quantity = quantity - 1 WHERE user_id = ? AND good_id = ?";
  private static int ONE_ITEM_CONST = 1;

  private ShoppingCartDaoImpl() {
  }

  public static ShoppingCartDaoImpl getInstance() {
    if (instance == null) {
      instance = new ShoppingCartDaoImpl();
    }
    return instance;
  }

  @Override
  public boolean add(ShoppingCartItem shoppingCartItem) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean add(Connection connection, Long userId, Long goodId) throws ServiceException { //todo logs
    try (PreparedStatement statement = connection.prepareStatement(INSERT_ITEM_INTO_SHOPPING_CART_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      statement.setInt(3, ONE_ITEM_CONST);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean incrementQuantity(Connection connection, Long userId, Long goodId) throws ServiceException {
    try (PreparedStatement statement = connection.prepareStatement(UPDATE_SHOPPING_CART_ITEM_QUANTITY_BY_ID_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean decrementQuantity(Connection connection, Long userId, Long goodId) throws ServiceException { // todo logs
    try (PreparedStatement statement = connection.prepareStatement(DECREMENT_QUANTITY_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      int result = statement.executeUpdate();
      return result > 0;
    } catch (SQLException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<ShoppingCartItem> getShoppingCartItem(Long userId, Long goodId) throws ServiceException {
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_SHOPPING_CART_ITEM_BY_ID_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      ResultSet resultSet = statement.executeQuery();
      Optional<ShoppingCartItem> optionalShoppingCartItem = Optional.empty();
      if (resultSet.next()) {
        Long quantity = resultSet.getLong(QUANTITY_PARAMETER);
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem(userId, goodId, quantity);
        optionalShoppingCartItem = Optional.of(shoppingCartItem);
      }
      return optionalShoppingCartItem;
    } catch (SQLException e) {
      throw new ServiceException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean exists(Long userId, Long goodId) throws ServiceException { // todo logs
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(EXISTS_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      ResultSet resultSet = statement.executeQuery();
      boolean exists = false;
      if (resultSet.next()) {
        exists = resultSet.getBoolean(1);
      }
      return exists;
    } catch (SQLException e) {
      throw new ServiceException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<ShoppingCartItemDto> findAllDtoByUserId(Long userId) throws DaoException { // todo logs
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_SHOPPING_CART_ITEMS_BY_USER_ID_WITH_GOOD_NAME_SQL)) {
      statement.setLong(1, userId);
      ResultSet resultSet = statement.executeQuery();
      List<ShoppingCartItemDto> shoppingCartItems = new ArrayList<>();
      while (resultSet.next()) {
        Long goodId = resultSet.getLong(GOOD_ID_PARAMETER);
        String goodName = resultSet.getString(NAME_PARAMETER);
        Long quantity = resultSet.getLong(QUANTITY_PARAMETER);
        Long pricePerOne = resultSet.getLong(PRICE_PARAMETER);
        Long fullPrice = pricePerOne * quantity;
        shoppingCartItems.add(new ShoppingCartItemDto(goodId, goodName, quantity, fullPrice));
      }
      return shoppingCartItems;
    } catch (SQLException e) {
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean deleteById(Long userId, Long goodId) throws DaoException { // todo logs
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(DELETE_SHOPPING_CART_ITEM_BY_ID_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      int resultSet = statement.executeUpdate();
      return resultSet > 0;
    } catch (SQLException e) {
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean update(ShoppingCartItem shoppingCartItem) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean delete(ShoppingCartItem shoppingCartItem) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean deleteById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<ShoppingCartItem> findAll() throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Optional<ShoppingCartItem> findById(Long id) throws DaoException { // todo logs
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean existsById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}