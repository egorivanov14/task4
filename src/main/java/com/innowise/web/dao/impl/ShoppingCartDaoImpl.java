package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.AbstractDao;
import com.innowise.web.dao.ShoppingCartDao;
import com.innowise.web.entity.ShoppingCartItem;
import com.innowise.web.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ShoppingCartDaoImpl extends AbstractDao<ShoppingCartItem> implements ShoppingCartDao{
  private static final Logger logger = LogManager.getLogger(ShoppingCartDaoImpl.class);
  private static ShoppingCartDaoImpl instance;
  private static final String ADD_ITEM_TO_SHOPPING_CART_SQL = "INSERT INTO shopping_cart (user_id, good_id, quantity, created_at) VALUES (?, ?, ?, ?)";

  private ShoppingCartDaoImpl() {}

  public static ShoppingCartDaoImpl getInstance() {
    if(instance == null) {
      instance = new ShoppingCartDaoImpl();
    }
    return instance;
  }

  @Override
  public boolean add(ShoppingCartItem shoppingCartItem) throws DaoException { //todo logs
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try(PreparedStatement statement = connection.prepareStatement(ADD_ITEM_TO_SHOPPING_CART_SQL)) {
      int resultSet = statement.executeUpdate();
      return resultSet > 0;
    } catch (SQLException e) {
      throw new DaoException(e);
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
  public Optional<ShoppingCartItem> findById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean existsById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}