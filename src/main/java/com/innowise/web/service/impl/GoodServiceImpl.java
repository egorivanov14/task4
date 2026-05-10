package com.innowise.web.service.impl;

import com.innowise.web.dao.impl.GoodDaoImpl;
import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.dto.GoodDetailDto;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.dto.UserDto;
import com.innowise.web.dto.converter.GoodConverter;
import com.innowise.web.entity.Good;
import com.innowise.web.entity.Role;
import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.GoodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GoodServiceImpl implements GoodService {
  private static final Logger logger = LogManager.getLogger(GoodServiceImpl.class);
  private static GoodServiceImpl instance;

  private GoodServiceImpl() {
  }

  public static GoodServiceImpl getInstance() {
    if (instance == null) {
      instance = new GoodServiceImpl();
    }
    return instance;
  }

  @Override
  public List<GoodDetailDto> getGoodDtoListWithUsername(Long adminId) throws ServiceException {
    logger.debug("Fetching all goods with usernames");
    try {
      UserDaoImpl userDao = UserDaoImpl.getInstance();
      User user = userDao.findById(adminId).orElseThrow(() -> new ServiceException("User not found"));
      int roleId = user.getRoleId();
      Role role = Role.defineRole(roleId);
      if (role.equals(Role.ROLE_ADMIN)) {
        GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
        return goodDao.findAllWithUsername();
      } else {
        throw new ServiceException("You dont have permission to use this service");
      }
    } catch (DaoException e) {
      logger.error(e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean add(Good good) throws ServiceException {
    logger.debug("Adding good: {}", good.getName());
    try {
      GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
      return goodDao.add(good);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean deleteById(Long goodId, UserDto currentUser) throws ServiceException {
    logger.debug("Deleting good by ID: {}", goodId);
    boolean result = false;
    try {
      GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
      Good good = goodDao.findById(goodId).orElseThrow(() -> new ServiceException("Good is not exist"));
      Long addedBy = good.getAddedBy();
      Long userId = currentUser.getId();
      boolean isOwner = Objects.equals(addedBy, userId);
      Role role = currentUser.getRole();
      boolean isAdmin = role.equals(Role.ROLE_ADMIN);
      if (isOwner || isAdmin) {
        result = goodDao.deleteById(goodId);
      }
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    return result;
  }

  @Override
  public List<GoodDto> getAvailableGoodDtoList() throws ServiceException {
    logger.debug("Fetching available goods list");
    GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
    try {
      List<Good> goodList = goodDao.findAllAvailable();
      GoodConverter goodConverter = new GoodConverter();
      List<GoodDto> result = goodList.stream().map(goodConverter::toDto).toList();
      logger.debug("Successfully fetched {} available goods", result.size());
      return result;
    } catch (DaoException e) {
      logger.error("Failed to fetch available goods list", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<GoodDto> getGoodDtoListByUserId(Long userId) throws ServiceException {
    try {
      GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
      List<Good> goodList = goodDao.findAllByAddedBy(userId);
      GoodConverter goodConverter = new GoodConverter();
      return goodList.stream().map(goodConverter::toDto).toList();
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean changeQuantity(Long userId, Long goodId, Long newQuantity) throws ServiceException {
    logger.debug("Changing quantity for good ID: {} to {} by user ID: {}", goodId, newQuantity, userId);
    GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
    try {
      Good good = goodDao.findById(goodId).orElseThrow(() -> new ServiceException("Good not exists"));
      Long addedBy = good.getAddedBy();
      boolean result = false;
      if (addedBy.equals(userId) && newQuantity >= 0) {
        result = goodDao.changeQuantity(goodId, newQuantity);
        logger.info("Successfully changed quantity for good ID: {} to {} by user ID: {}", goodId, newQuantity, userId);
      } else {
        logger.warn("Quantity change denied for good ID: {} by user ID: {} (owner: {}, newQuantity: {})", goodId, userId, addedBy, newQuantity);
      }
      return result;
    } catch (DaoException e) {
      logger.error("Failed to change quantity for good ID: {} by user ID: {}", goodId, userId, e);
      throw new ServiceException(e);
    }
  }
}