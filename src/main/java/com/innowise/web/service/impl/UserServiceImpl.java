package com.innowise.web.service.impl;

import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
  private static final Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());
  public static UserServiceImpl instance;

  private UserServiceImpl() {
  }

  public static UserServiceImpl getInstance() {
    if (instance == null) {
      instance = new UserServiceImpl();
    }
    return instance;
  }

  @Override
  public boolean register(String username, String password) throws ServiceException {
    logger.info("UserServiceImpl trying to register user: {}", username);
    if(username == null || password == null) {
      throw new ServiceException("Username or password is null.");// todo
    }
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    boolean result = false;
    try {
      boolean isUserExists = userDao.existsByUsername(username);

      if (!isUserExists) {
        logger.info("UserServiceImpl register user: {}", username);
        User user = new User(username, password);
        result = userDao.add(user);
      }
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    return result;
  }

  @Override
  public boolean login(String username, String password) throws ServiceException {
    logger.info("UserServiceImpl trying to login user: {}", username);
    if(username == null || password == null) {
      throw new ServiceException("Username or password is null.");// todo
    }
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    Optional<User> userOptional;
    try {
      userOptional = userDao.findByUsername(username);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    boolean result = false;

    if (userOptional.isPresent()) {
      logger.info("UserServiceImpl login user: {}", username);
      User user = userOptional.get();
      result = user.getPassword().equals(password); //todo
    }
    return result;
  }
}