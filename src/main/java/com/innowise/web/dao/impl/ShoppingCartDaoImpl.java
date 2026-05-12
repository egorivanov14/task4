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
  private static final String EXISTS_SQL = "SELECT EXISTS (SELECT 1 FROM shopping_cart WHERE user_id = ? AND good_id = ?)";
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
  public boolean add(Connection connection, Long userId, Long goodId) throws ServiceException {
    logger.debug("Adding good ID: {} to cart of user ID: {}", goodId, userId);
    try (PreparedStatement statement = connection.prepareStatement(INSERT_ITEM_INTO_SHOPPING_CART_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      statement.setInt(3, ONE_ITEM_CONST);
      int result = statement.executeUpdate();
      logger.debug("Add to cart result for user ID: {}, good ID: {}: {}", userId, goodId, result > 0);
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to add good ID: {} to cart of user ID: {}", goodId, userId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean incrementQuantity(Connection connection, Long userId, Long goodId) throws ServiceException {
    logger.debug("Incrementing quantity for good ID: {} in cart of user ID: {}", goodId, userId);
    try (PreparedStatement statement = connection.prepareStatement(UPDATE_SHOPPING_CART_ITEM_QUANTITY_BY_ID_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      int result = statement.executeUpdate();
      logger.debug("Increment quantity result for user ID: {}, good ID: {}: {}", userId, goodId, result > 0);
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to increment quantity for good ID: {} in cart of user ID: {}", goodId, userId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean decrementQuantity(Connection connection, Long userId, Long goodId) throws ServiceException {
    logger.debug("Decrementing quantity for good ID: {} in cart of user ID: {}", goodId, userId);
    try (PreparedStatement statement = connection.prepareStatement(DECREMENT_QUANTITY_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      int result = statement.executeUpdate();
      logger.debug("Decrement quantity result for user ID: {}, good ID: {}: {}", userId, goodId, result > 0);
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to decrement quantity for good ID: {} in cart of user ID: {}", goodId, userId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<ShoppingCartItem> getShoppingCartItem(Long userId, Long goodId) throws ServiceException {
    logger.debug("Fetching cart item for user ID: {}, good ID: {}", userId, goodId);
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
        logger.debug("Found cart item with quantity: {}", quantity);
      } else {
        logger.debug("Cart item not found for user ID: {}, good ID: {}", userId, goodId);
      }
      return optionalShoppingCartItem;
    } catch (SQLException e) {
      logger.error("Failed to fetch cart item for user ID: {}, good ID: {}", userId, goodId, e);
      throw new ServiceException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean exists(Long userId, Long goodId) throws ServiceException {
    logger.debug("Checking existence in cart for user ID: {}, good ID: {}", userId, goodId);
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
      logger.debug("Cart item exists for user ID: {}, good ID: {}: {}", userId, goodId, exists);
      return exists;
    } catch (SQLException e) {
      logger.error("Database error while checking cart existence for user ID: {}, good ID: {}", userId, goodId, e);
      throw new ServiceException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<ShoppingCartItemDto> findAllDtoByUserId(Long userId) throws DaoException {
    logger.debug("Fetching all cart items for user ID: {}", userId);
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
        Long amount = pricePerOne * quantity;
        shoppingCartItems.add(new ShoppingCartItemDto(goodId, goodName, quantity, amount));
      }
      logger.debug("Successfully fetched {} cart items for user ID: {}", shoppingCartItems.size(), userId);
      return shoppingCartItems;
    } catch (SQLException e) {
      logger.error("Failed to fetch cart items for user ID: {}", userId, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean deleteById(Connection connection, Long userId, Long goodId) throws DaoException {
    logger.debug("Deleting cart item for user ID: {}, good ID: {}", userId, goodId);
    try (PreparedStatement statement = connection.prepareStatement(DELETE_SHOPPING_CART_ITEM_BY_ID_SQL)) {
      statement.setLong(1, userId);
      statement.setLong(2, goodId);
      int resultSet = statement.executeUpdate();
      logger.debug("Delete cart item result for user ID: {}, good ID: {}: {}", userId, goodId, resultSet > 0);
      return resultSet > 0;
    } catch (SQLException e) {
      logger.error("Failed to delete cart item for user ID: {}, good ID: {}", userId, goodId, e);
      throw new DaoException(e);
    }
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
  public Optional<ShoppingCartItem> findById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean existsById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}