package com.innowise.web.service.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.impl.GoodDaoImpl;
import com.innowise.web.dao.impl.ShoppingCartDaoImpl;
import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.dto.ShoppingCartItemDto;
import com.innowise.web.entity.ShoppingCartItem;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.ShoppingCartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ShoppingCartServiceImpl implements ShoppingCartService {
  private static final Logger logger = LogManager.getLogger(ShoppingCartServiceImpl.class);
  private static ShoppingCartServiceImpl instance;

  private ShoppingCartServiceImpl() {
  }

  public static ShoppingCartServiceImpl getInstance() {
    if (instance == null) {
      instance = new ShoppingCartServiceImpl();
    }
    return instance;
  }

  @Override
  public boolean addItem(Long userId, Long goodId) throws ServiceException {
    logger.debug("Adding good ID: {} to shopping_cart of user ID: {}", goodId, userId);

    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try {
      UserDaoImpl userDao = UserDaoImpl.getInstance();
      if (!userDao.existsById(userId)) {
        logger.warn("Add to shopping_cart failed: user ID {} not found", userId);
        return false;
      }
      GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
      if (!goodDao.existsById(goodId)) {
        logger.warn("Add to shopping_cart failed: good ID {} not found", goodId);
        return false;
      }
      connection.setAutoCommit(false);
      boolean isReserved = goodDao.decrementQuantity(connection, userId, goodId);
      boolean result = false;
      if (isReserved) {
        logger.debug("Stock reserved for good ID: {}", goodId);
        ShoppingCartDaoImpl shoppingCartDao = ShoppingCartDaoImpl.getInstance();
        if (shoppingCartDao.exists(userId, goodId)) {
          result = shoppingCartDao.incrementQuantity(connection, userId, goodId);
          logger.debug("Updated quantity for good ID: {} in cart", goodId);
        } else {
          result = shoppingCartDao.add(connection, userId, goodId);
          logger.debug("Added new item good ID: {} to cart", goodId);
        }
        if (result) {
          connection.commit();
          logger.info("Successfully added good ID: {} to shopping_cart of user ID: {}", goodId, userId);
        } else {
          logger.warn("shopping_cart operation failed for good ID: {}, rolling back", goodId);
          connection.rollback();
        }
      } else {
        logger.warn("Failed to reserve stock for good ID: {}", goodId);
        connection.rollback();
      }
      return result;
    } catch (SQLException | DaoException e) {
      logger.error("Failed to addItem good ID: {} to shopping_cart of user ID: {}", goodId, userId, e);
      try {
        connection.rollback();
      } catch (SQLException ignore) {
      }
      throw new ServiceException(e);
    } finally {
      try {
        connection.setAutoCommit(true);
      } catch (SQLException ignore) {
      }
      connectionPool.releaseConnection(connection);
      logger.debug("Connection released for user ID: {}, good ID: {}", userId, goodId);
    }
  }

  @Override
  public List<ShoppingCartItemDto> findAllDtoByUserId(Long userId) throws ServiceException {
    try {
      ShoppingCartDaoImpl shoppingCartDao = ShoppingCartDaoImpl.getInstance();
      return shoppingCartDao.findAllDtoByUserId(userId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean removeItem(Long userId, Long goodId) throws ServiceException {
    logger.debug("Removing good ID: {} from cart of user ID: {}", goodId, userId);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try {
      ShoppingCartDaoImpl shoppingCartDao = ShoppingCartDaoImpl.getInstance();
      connection.setAutoCommit(false);
      ShoppingCartItem shoppingCartItem = shoppingCartDao.getShoppingCartItem(userId, goodId)
              .orElseThrow(() -> new ServiceException("Good ID: " + goodId + " not found"));
      Long quantity = shoppingCartItem.getQuantity();
      boolean isItemRemoved;
      if (quantity > 1) {
        logger.debug("Decrementing quantity for good ID: {} (current: {})", goodId, quantity);
        isItemRemoved = shoppingCartDao.decrementQuantity(connection, userId, goodId);
      } else {
        logger.debug("Removing item completely for good ID: {} (quantity was 1)", goodId);
        isItemRemoved = shoppingCartDao.deleteById(userId, goodId);
      }
      boolean result = false;
      if (isItemRemoved) {
        GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
        boolean isAddedToGoodWarehouse = goodDao.incrementQuantity(connection, goodId);
        if (isAddedToGoodWarehouse) {
          result = true;
          connection.commit();
          logger.info("Successfully removed good ID: {} from cart of user ID: {}", goodId, userId);
        } else {
          logger.warn("Failed to return good ID: {} to warehouse, rolling back", goodId);
          connection.rollback();
        }
      } else {
        logger.warn("Failed to remove good ID: {} from cart, rolling back", goodId);
        connection.rollback();
      }
      return result;
    } catch (SQLException | DaoException e) {
      try {
        connection.rollback();
      } catch (SQLException ignore) {
        logger.warn("Secondary rollback failed for user ID: {}, good ID: {}", userId, goodId, ignore);
      }
      logger.error("Transaction failed while removing good ID: {} from cart of user ID: {}", goodId, userId, e);
      throw new ServiceException(e);
    } finally {
      try {
        connection.setAutoCommit(true);
      } catch (SQLException ignore) {
      }
      connectionPool.releaseConnection(connection);
      logger.debug("Connection released for user ID: {}, good ID: {}", userId, goodId);
    }
  }
}