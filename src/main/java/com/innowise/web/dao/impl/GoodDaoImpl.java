package com.innowise.web.dao.impl;

import com.innowise.web.connection.ConnectionPool;
import com.innowise.web.dao.AbstractDao;
import com.innowise.web.dao.GoodDao;
import com.innowise.web.dto.GoodDetailDto;
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
  private static final String ADD_GOOD_SQL = "INSERT INTO goods (name, price, quantity, manufacturer, description, added_by) VALUES (?, ?, ?, ?, ?, ?)";
  private static final String GET_ALL_GOODS_WITH_USERNAME_SQL = "SELECT g.id, g.name, g.price, g.quantity, g.manufacturer, g.description, u.user_name FROM goods g INNER JOIN users u ON g.added_by = u.id";
  private static final String DELETE_GOOD_BY_ID_SQL = "DELETE FROM goods WHERE id = ?";
  private static final String GET_GOOD_BY_ID_SQL = "SELECT id,  name, price, quantity, manufacturer, description, added_by FROM goods WHERE id = ?";
  private static final String GET_ALL_GOOD_SQL = "SELECT id,  name, price, quantity, manufacturer, description, added_by FROM goods";
  private static final String GET_ALL_GOOD_BY_ADDED_BY_SQL = "SELECT id,  name, price, quantity, manufacturer, description, added_by FROM goods WHERE added_by = ?";
  private static final String EXISTS_BY_ID_SQL = "SELECT EXISTS (SELECT 1 FROM goods WHERE id = ?)";
  private static final String GET_ALL_AVAILABLE_GOOD_SQL = "SELECT id,  name, price, quantity, manufacturer, description, added_by FROM goods WHERE quantity > 0";
  private static final String RESERVE_GOOD_BY_ID_SQL = "UPDATE goods SET quantity = quantity - 1 WHERE id = ? AND quantity >= 1";
  private static final String ADD_QUANTITY_BY_ID_SQL = "UPDATE goods SET quantity = quantity + 1 WHERE id = ?";
  private static final String CHANGE_QUANTITY_BY_ID_SQL = "UPDATE goods SET quantity = ? WHERE id = ?";

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
    logger.debug("Attempting to addItem good: {}", good.getName());
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    Long quantity = good.getQuantity();
    if (quantity > 0) {
      try (PreparedStatement statement = connection.prepareStatement(ADD_GOOD_SQL)) {
        String name = good.getName();
        Long price = good.getPrice();
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
        logger.error("Failed to addItem good '{}'", good.getName(), e);
        throw new DaoException(e);
      } finally {
        connectionPool.releaseConnection(connection);
      }
    }
    return false;
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
      logger.error("Failed to deleteById good with ID: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Good> findAll() throws DaoException {
    logger.debug("Fetching all goods");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_GOOD_SQL);
         ResultSet resultSet = statement.executeQuery()) {
      List<Good> result = getGoodListFromResultSet(resultSet);
      logger.debug("Successfully fetched {} goods", result.size());
      return result;
    } catch (SQLException e) {
      logger.error("Failed to fetch all goods", e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public Optional<Good> findById(Long id) throws DaoException {
    logger.debug("Fetching good by ID: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_GOOD_BY_ID_SQL)) {
      statement.setLong(1, id);
      try (ResultSet resultSet = statement.executeQuery()) {
        Optional<Good> optionalGood = Optional.empty();
        if (resultSet.next()) {
          Good good = createGoodFromResultSet(resultSet);
          optionalGood = Optional.of(good);
          logger.debug("Found good: {}", good.getName());
        } else {
          logger.debug("Good with ID: {} not found", id);
        }
        return optionalGood;
      }
    } catch (SQLException e) {
      logger.error("Failed to fetch good by ID: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean existsById(Long id) throws DaoException {
    logger.debug("Checking existence for good ID: {}", id);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(EXISTS_BY_ID_SQL)) {
      statement.setLong(1, id);
      ResultSet resultSet = statement.executeQuery();
      boolean exists = false;
      if (resultSet.next()) {
        exists = resultSet.getBoolean(1);
      }
      logger.debug("Good ID: {} exists: {}", id, exists);
      return exists;
    } catch (SQLException e) {
      logger.error("Database error while checking existence for good ID: {}", id, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<Good> findAllByAddedBy(Long userId) throws DaoException {
    logger.debug("Fetching goods for user ID: {}", userId);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_GOOD_BY_ADDED_BY_SQL)) {
      statement.setLong(1, userId);
      try (ResultSet resultSet = statement.executeQuery()) {
        logger.debug("Successfully fetched goods for user ID: {}", userId);
        return getGoodListFromResultSet(resultSet);
      }
    } catch (SQLException e) {
      logger.error("Failed to fetch goods for user ID: {}", userId, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public List<GoodDetailDto> findAllWithUsername() throws DaoException {
    logger.debug("Fetching all goods with usernames");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    List<GoodDetailDto> goodDetailDtoList = new ArrayList<>();
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

        GoodDetailDto goodDetailDto = new GoodDetailDto(id, name, price, quantity, manufacturer, description, addedBy);
        goodDetailDtoList.add(goodDetailDto);
      }
      logger.debug("Successfully retrieved {} goods", goodDetailDtoList.size());
    } catch (SQLException e) {
      logger.error("Failed to fetch all goods with usernames", e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
    return goodDetailDtoList;
  }

  @Override
  public List<Good> findAllAvailable() throws DaoException {
    logger.debug("Fetching all available goods");
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(GET_ALL_AVAILABLE_GOOD_SQL);
         ResultSet resultSet = statement.executeQuery()) {
      logger.debug("Successfully fetched available goods");
      return getGoodListFromResultSet(resultSet);
    } catch (SQLException e) {
      logger.error("Failed to fetch available goods", e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  @Override
  public boolean decrementQuantity(Connection connection, Long userId, Long goodId) throws DaoException {
    logger.debug("Decrementing quantity for good ID: {} by user ID: {}", goodId, userId);
    try (PreparedStatement statement = connection.prepareStatement(RESERVE_GOOD_BY_ID_SQL)) {
      statement.setLong(1, goodId);
      int result = statement.executeUpdate();
      logger.debug("Decrement result for good ID: {}: {}", goodId, result > 0);
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to decrement quantity for good ID: {}", goodId, e);
      throw new DaoException(e);
    }
  }

  @Override
  public boolean incrementQuantity(Connection connection, Long goodId) throws DaoException {
    logger.debug("Incrementing quantity for good ID: {}", goodId);
    try (PreparedStatement statement = connection.prepareStatement(ADD_QUANTITY_BY_ID_SQL)) {
      statement.setLong(1, goodId);
      int result = statement.executeUpdate();
      logger.debug("Increment result for good ID: {}: {}", goodId, result > 0);
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to increment quantity for good ID: {}", goodId, e);
      throw new DaoException(e);
    }
  }

  @Override
  public boolean changeQuantity(Long goodId, Long quantity) throws DaoException {
    logger.debug("Changing quantity for good ID: {} to {}", goodId, quantity);
    ConnectionPool connectionPool = ConnectionPool.getInstance();
    Connection connection = connectionPool.getConnection();
    try (PreparedStatement statement = connection.prepareStatement(CHANGE_QUANTITY_BY_ID_SQL)) {
      statement.setLong(1, quantity);
      statement.setLong(2, goodId);
      int result = statement.executeUpdate();
      logger.debug("Change quantity result for good ID: {}: {}", goodId, result > 0);
      return result > 0;
    } catch (SQLException e) {
      logger.error("Failed to change quantity for good ID: {}", goodId, e);
      throw new DaoException(e);
    } finally {
      connectionPool.releaseConnection(connection);
    }
  }

  private Good createGoodFromResultSet(ResultSet resultSet) throws SQLException {
    Long id = resultSet.getLong(ID_PARAMETER);
    String name = resultSet.getString(NAME_PARAMETER);
    Long price = resultSet.getLong(PRICE_PARAMETER);
    Long quantity = resultSet.getLong(QUANTITY_PARAMETER);
    String manufacturer = resultSet.getString(MANUFACTURER_PARAMETER);
    String description = resultSet.getString(DESCRIPTION_PARAMETER);
    Long added_by = resultSet.getLong(ADDED_BY_PARAMETER);

    return new Good(id, name, price, quantity, manufacturer, description, added_by);
  }

  private List<Good> getGoodListFromResultSet(ResultSet resultSet) throws SQLException {
    List<Good> goodList = new ArrayList<>();
    while (resultSet.next()) {
      Good good = createGoodFromResultSet(resultSet);
      goodList.add(good);
    }
    logger.debug("Retrieved {} goods", goodList.size());
    return goodList;
  }
}