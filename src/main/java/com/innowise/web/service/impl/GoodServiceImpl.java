package com.innowise.web.service.impl;

import com.innowise.web.dao.impl.GoodDaoImpl;
import com.innowise.web.dto.GoodDetailDto;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.dto.converter.GoodConverter;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.GoodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
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
  public List<GoodDetailDto> findAllWithUsername() throws ServiceException {
    logger.debug("Fetching all goods with usernames");
    try {
      GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
      return goodDao.findAllWithUsername();
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
  public boolean deleteById(Long id) throws ServiceException {
    logger.debug("Deleting good by ID: {}", id);
    try {
      GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
      return goodDao.deleteById(id);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public List<GoodDto> findAllGoodDto() throws ServiceException {//todo logs
    GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
    try {
      List<Good> goodList = goodDao.findAll();
      GoodConverter goodConverter = new GoodConverter();
      return goodList.stream().map(goodConverter::toDto).toList();
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<Good> findById(Long id) throws ServiceException {
    try {
      GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
      return goodDao.findById(id);
    } catch (DaoException e) {
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
}