package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.AbstractDao;
import com.innowise.web.dao.GoodDao;
import com.innowise.web.entity.Good;
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

public class GoodDaoImpl extends AbstractDao<Good> implements GoodDao {
  private static final Logger logger = LogManager.getLogger(GoodDaoImpl.class);
  private static GoodDaoImpl instance;
  private static final String GET_ALL_GOODS_SQL = "SELECT g.id, g.name, g.price, g.quantity, g.manufacturer, g.description FROM goods g";
  private static final String ADD_GOOD_SQL = "INSERT INTO goods (name, price, quantity, manufacturer, description) VALUES (?, ?, ?, ?, ?)";

  private GoodDaoImpl() {
  }

  public static GoodDaoImpl getInstance() {
    if (instance == null) {
      instance = new GoodDaoImpl();
    }
    return instance;
  }

  @Override
  public boolean add(Good good) throws DaoException {
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    boolean result = false;
    try (PreparedStatement statement = connection.prepareStatement(ADD_GOOD_SQL)) {
      String name = good.getName();
      Long price = good.getPrice();
      Long quantity = good.getQuantity();
      String manufactured = good.getManufacturer();
      String description = good.getDescription();
      statement.setString(1, name);
      statement.setLong(2, price);
      statement.setLong(3, quantity);
      statement.setString(4, manufactured);
      statement.setString(5, description);
      int resultSet = statement.executeUpdate();
      result = resultSet > 0;
    } catch (SQLException e) {
      throw new DaoException(e);
    }
    return result;
  }

  @Override
  public boolean update(Good good) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean delete(Good good) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<Good> findAll() throws DaoException {
    logger.info("Finding all Goods");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    List<Good> goods = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_GOODS_SQL);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        Long id = resultSet.getLong(1);
        String name = resultSet.getString(2);
        Long price = resultSet.getLong(3);
        Long quantity = resultSet.getLong(4);
        String manufacturer = resultSet.getString(5);
        String description = resultSet.getString(6);
        Good good = new Good(id, name, price, quantity, manufacturer, description);
        goods.add(good);
      }
    } catch (SQLException e) {
      logger.error(e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return goods;
  }

  @Override
  public Optional<Good> findById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}