package com.innowise.web.service.impl;

import com.innowise.web.dao.impl.GoodDaoImpl;
import com.innowise.web.dao.impl.ShoppingCartDaoImpl;
import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.entity.ShoppingCartItem;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.ShoppingCartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
  public boolean add(ShoppingCartItem item) throws ServiceException { //todo logs
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    GoodDaoImpl goodDao = GoodDaoImpl.getInstance();
    Long userId = item.getUserId();
    Long goodId = item.getGoodId();
    try {
      boolean result = false;
      if (userDao.existsById(userId) && goodDao.existsById(goodId)) {
        ShoppingCartDaoImpl shoppingCartDao = ShoppingCartDaoImpl.getInstance();
        result = shoppingCartDao.add(item);
      }
      return result;
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }
}