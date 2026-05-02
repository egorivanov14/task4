package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.AbstractDao;
import com.innowise.web.dao.GoodDao;
import com.innowise.web.dto.GoodDto;
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

import static com.innowise.web.config.PublicConstants.*;

public class GoodDaoImpl extends AbstractDao<Good> implements GoodDao {
  private static final Logger logger = LogManager.getLogger(GoodDaoImpl.class);
  private static GoodDaoImpl instance;
  private static final String GET_ALL_GOODS_SQL = "SELECT g.id, g.name, g.price, g.quantity, g.manufacturer, g.description, g.added_by FROM goods g";
  private static final String ADD_GOOD_SQL = "INSERT INTO goods (name, price, quantity, manufacturer, description, added_by) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_GOODS_WITH_USERNAME_SQL = "SELECT g.id, g.name, g.price, g.quantity, g.manufacturer, g.description, u.user_name FROM goods g INNER JOIN users u ON g.added_by = u.id";
  private static final String GET_ALL_GOODS_BY_USER_ID_SQL = "SELECT id, name, price, quantity, manufacturer FROM goods WHERE added_by = ?";
  private static final String DELETE_GOOD_BY_ID_SQL = "DELETE FROM goods WHERE id = ?";

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
    logger.debug("Attempting to add good: {}", good.getName());
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(ADD_GOOD_SQL)) {
      String name = good.getName();
      Long price = good.getPrice();
      Long quantity = good.getQuantity();
      String manufactured = good.getManufacturer();
      String description = good.getDescription();
      Long addedBy = good.getAddedBy();
      statement.setString(1, name);
      statement.setLong(2, price);
      statement.setLong(3, quantity);
      statement.setString(4, manufactured);
      statement.setString(5, description);
      statement.setLong(6, addedBy);
      int resultSet = statement.executeUpdate();
      logger.debug("Successfully added good: {}", good.getName());
      return resultSet > 0;
    } catch (SQLException e) {
      logger.error("Failed to add good '{}'", good.getName(), e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
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
  public boolean deleteById(Long id) throws DaoException {
    logger.debug("Deleting good by ID: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(DELETE_GOOD_BY_ID_SQL)) {
      statement.setLong(1, id);
      int resultSet = statement.executeUpdate();
      return resultSet > 0;
    } catch (SQLException e) {
      logger.error("Failed to delete good with ID: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Good> findAll() throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Optional<Good> findById(Long id) throws DaoException {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<GoodDto> getGoodDtoListByUserId(Long userId) throws DaoException {
    logger.debug("Fetching goods for user ID: {}", userId);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    List<GoodDto> goodDtoList = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_GOODS_BY_USER_ID_SQL)) {
      statement.setLong(1, userId);
      try (ResultSet resultSet = statement.executeQuery()) {
        while (resultSet.next()) {
          Long id = resultSet.getLong(ID_PARAMETER);
          String name = resultSet.getString(NAME_PARAMETER);
          Long price = resultSet.getLong(PRICE_PARAMETER);
          Long quantity = resultSet.getLong(QUANTITY_PARAMETER);
          String manufacturer = resultSet.getString(MANUFACTURER_PARAMETER);

          GoodDto goodDto = new GoodDto();
          goodDto.setId(id);
          goodDto.setName(name);
          goodDto.setPrice(price);
          goodDto.setQuantity(quantity);
          goodDto.setManufacturer(manufacturer);
          goodDtoList.add(goodDto);
        }
        logger.debug("Retrieved {} goods for user ID: {}", goodDtoList.size(), userId);
      }
    } catch (SQLException e) {
      logger.error("Failed to fetch goods for user ID: {}", userId, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return goodDtoList;
  }

  @Override
  public List<GoodDto> findAllWithUsername() throws DaoException {
    logger.debug("Fetching all goods with usernames");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    List<GoodDto> goodDtoList = new ArrayList<>();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_GOODS_WITH_USERNAME_SQL);
         ResultSet resultSet = statement.executeQuery()) {
      while (resultSet.next()) {
        Long id = resultSet.getLong(ID_PARAMETER);
        String name = resultSet.getString(NAME_PARAMETER);
        Long price = resultSet.getLong(PRICE_PARAMETER);
        Long quantity = resultSet.getLong(QUANTITY_PARAMETER);
        String manufacturer = resultSet.getString(MANUFACTURER_PARAMETER);
        String description = resultSet.getString(DESCRIPTION_PARAMETER);
        String addedBy = resultSet.getString(USERNAME_SQL_PARAMETER);

        GoodDto goodDto = new GoodDto(id, name, price, quantity, manufacturer, description, addedBy);
        goodDtoList.add(goodDto);
      }
      logger.debug("Successfully retrieved {} goods", goodDtoList.size());
    } catch (SQLException e) {
      logger.error("Failed to fetch all goods with usernames", e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return goodDtoList;
  }
}