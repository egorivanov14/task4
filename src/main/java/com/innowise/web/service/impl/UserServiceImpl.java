package com.innowise.web.service.impl;

import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.dto.Converter;
import com.innowise.web.dto.UserDto;
import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.service.Role;
import com.innowise.web.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import static com.innowise.web.service.Role.defineRole;

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
    if (username == null || password == null) {
      logger.info("UserServiceImpl user trying to register with null values");
      return false;
    }
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    boolean isRegistered = false;
    try {
      boolean isUserExists = userDao.existsByUsername(username);
      if (!isUserExists) {
        logger.info("UserServiceImpl register user: {}", username);
        userDao.add(new User(username, password));
        isRegistered = true;
      }
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    return isRegistered;
  }

  @Override
  public boolean login(String username, String password) throws ServiceException {
    logger.info("UserServiceImpl trying to login user: {}", username);
    if (username == null || password == null) {
      logger.info("UserServiceImpl user trying to login with null values");
      return false;
    }
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    Optional<User> userOptional;
    boolean isAuthenticated = false;
    try {
      userOptional = userDao.findByUsername(username);
      if (userOptional.isPresent()) {
        logger.info("UserServiceImpl login user: {}", username);
        User user = userOptional.get();
        String userPassword = user.getPassword();
        isAuthenticated = userPassword.equals(password);
      }
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    return isAuthenticated;
  }

  @Override
  public boolean setRole(String adminUsername, String toUsername, String role) throws ServiceException {
    logger.info("UserServiceImpl trying to set role for user: {}", toUsername);
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    boolean result = false;
    try {
      User user = userDao.findByUsername(adminUsername).orElseThrow(
              () -> new ServiceException("Admin user not found"));
      Role currentUserRole = user.getRole();
      Role roleAdmin = Role.ROLE_ADMIN;
      if (currentUserRole.equals(roleAdmin) && !adminUsername.equals(toUsername)) {
        if (userDao.existsByUsername(toUsername)) {
          int roleId = defineRole(role);
          result = userDao.updateRole(toUsername, roleId);
        } else {
          throw new ServiceException("User not found.");
        }
      }
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    logger.info("UserServiceImpl set role for user: {}", toUsername);
    return result;
  }

  @Override
  public Optional<User> getUser(String username) throws ServiceException {
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    Optional<User> userOptional;
    try {
      userOptional = userDao.findByUsername(username);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    return userOptional;
  }

  @Override
  public Optional<UserDto> getUserDto(String username) throws ServiceException {
    logger.info("UserServiceImpl getUserDto for user: {}", username);
    Optional<User> userOptional = getUser(username);
    Optional<UserDto> userDtoOptional = Optional.empty();
    if (userOptional.isPresent()) {
      logger.info("UserServiceImpl user found: {}", username);
      Converter converter = Converter.getInstance();
      User user = userOptional.get();
      UserDto userDto = converter.convertUserToDto(user);
      userDtoOptional = Optional.of(userDto);
    }
    return userDtoOptional;
  }

  @Override
  public List<User> getUsers() throws ServiceException {
    logger.info("UserServiceImpl getUsers");
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    List<User> users;
    try {
      users = userDao.findAll();
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
    return users;
  }

  @Override
  public List<UserDto> getUserDtoList() throws ServiceException {
    logger.info("UserServiceImpl getUserDtoList");
    List<User> users = getUsers();
    Converter converter = Converter.getInstance();
    return users.stream().map(converter::convertUserToDto).toList();
  }

  @Override
  public boolean deleteUserByUsername(String username) throws ServiceException {
    logger.info("UserServiceImpl deleteUserByUsername for user: {}", username);
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      return userDao.deleteUserByUsername(username);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean updateUsername(Long id, String newUsername) throws ServiceException {
    logger.info("UserServiceImpl updateUsername for user with id: {}", id);
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      return userDao.updateUsername(id, newUsername);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }
}