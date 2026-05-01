package com.innowise.web.service.impl;

import com.innowise.web.dao.impl.GoodDaoImpl;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.GoodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
  public List<Good> findAll() throws ServiceException {
    logger.info("Finding all goods");
    GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
    try {
      return goodDao.findAll();
    } catch (DaoException e) {
      logger.error(e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<GoodDto> findAllDto() throws ServiceException {
    GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
    try {
      return goodDao.findAllGoodDto();
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean add(Good good) throws ServiceException {
    logger.info("Adding good");
    GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
    try {
      return goodDao.add(good);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }
}