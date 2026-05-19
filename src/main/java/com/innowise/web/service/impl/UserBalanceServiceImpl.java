package com.innowise.web.service.impl;

import com.innowise.web.dao.UserBalanceDao;
import com.innowise.web.dao.impl.UserBalanceDaoImpl;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.UserBalanceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserBalanceServiceImpl implements UserBalanceService {
  private static final Logger logger = LogManager.getLogger(UserBalanceServiceImpl.class);
  private static UserBalanceServiceImpl instance;

  private UserBalanceServiceImpl() {
  }

  public static UserBalanceServiceImpl getInstance() {
    if (instance == null) {
      instance = new UserBalanceServiceImpl();
    }
    return instance;
  }

  @Override
  public Long getBalanceByUserId(Long userId) throws ServiceException { // todo logs
    UserBalanceDao userBalanceDao = UserBalanceDaoImpl.getInstance();
    try {
      return userBalanceDao.getBalanceByUserId(userId);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean uppBalance(Long userId, Long amount) throws ServiceException { // todo logs
    if (amount <= 0) {
      return false;
    }
    UserBalanceDaoImpl userBalanceDao = UserBalanceDaoImpl.getInstance();
    try {
      return userBalanceDao.updateBalanceByUserId(userId, amount);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }
}