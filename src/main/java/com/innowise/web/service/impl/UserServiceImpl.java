package com.innowise.web.service.impl;

import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.dto.UserDto;
import com.innowise.web.dto.converter.UserConverter;
import com.innowise.web.entity.Role;
import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;
import com.innowise.web.security.PasswordCoder;
import com.innowise.web.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.innowise.web.entity.Role.defineRole;

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
    logger.debug("Registration attempt for user: {}", username);
    if (username == null || password == null) {
      logger.warn("Registration failed: null credentials provided");
      return false;
    }
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    boolean isRegistered = false;
    try {
      boolean isUserExists = userDao.existsByUsername(username);
      if (!isUserExists) {
        logger.warn("Registration failed: user '{}' already exists", username);
        String passwordHash = PasswordCoder.encode(password);
        Role roleUser = Role.ROLE_USER;
        int roleUserId = roleUser.getRoleId();
        User user = new User(null, username, passwordHash, roleUserId);
        userDao.add(user);
        logger.info("User successfully registered: {}", username);
        isRegistered = true;
      }
    } catch (DaoException e) {
      logger.error("Service error during registration of user '{}'", username, e);
      throw new ServiceException(e);
    }
    return isRegistered;
  }

  @Override
  public boolean login(String username, String password) throws ServiceException {
    logger.debug("Login attempt for user: {}", username);
    if (username == null || password == null) {
      logger.warn("Login failed: null credentials provided");
      return false;
    }
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      Optional<User> userOptional = userDao.findByUsername(username);
      boolean isAuthenticated = false;
      if (userOptional.isPresent()) {
        logger.info("User successfully authenticated: {}", username);
        User user = userOptional.get();
        String userPassword = user.getPassword();
        isAuthenticated = PasswordCoder.checkPassword(password, userPassword);
      }
      return isAuthenticated;
    } catch (DaoException e) {
      logger.error("Service error during login for user '{}'", username, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean setRole(UserDto admin, String toUsername, String role) throws ServiceException {
    logger.debug("Role change attempt: admin '{}' -> user '{}', target role: {}", admin.getUsername(), toUsername, role);
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      Role adminRole = admin.getRole();
      String adminUsername = admin.getUsername();
      boolean result = false;
      if (adminRole.equals(Role.ROLE_ADMIN) && !adminUsername.equals(toUsername)) {
        if (userDao.existsByUsername(toUsername)) {
          int roleId = defineRole(role);
          result = userDao.updateRole(toUsername, roleId);
        }
      }
      return result;
    } catch (DaoException e) {
      logger.error("Service error during role assignment for user '{}'", toUsername, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<User> getUser(String username) throws ServiceException {
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      return userDao.findByUsername(username);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public Optional<UserDto> getUserDto(String username) throws ServiceException {
    logger.debug("Fetching DTO for user: {}", username);
    Optional<User> userOptional = getUser(username);
    Optional<UserDto> userDtoOptional = Optional.empty();
    if (userOptional.isPresent()) {
      logger.debug("Found user: {}", username);
      UserConverter userConverter = new UserConverter();
      userDtoOptional = userOptional.map(userConverter::toDto);
    }
    return userDtoOptional;
  }

  @Override
  public List<User> getUsers() throws ServiceException {
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      return userDao.findAll();
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public List<UserDto> getUserDtoList(Long adminId) throws ServiceException {
    logger.debug("Fetching all user DTOs");
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      User user = userDao.findById(adminId).orElseThrow(() -> new ServiceException("User not found"));
      int roleId = user.getRoleId();
      Role role = Role.defineRole(roleId);
      if (role.equals(Role.ROLE_ADMIN)) {
        List<User> users = getUsers();
        UserConverter userConverter = new UserConverter();
        return users.stream().map(userConverter::toDto).toList();
      } else {
        throw new ServiceException("You dont have permission to do permit action");
      }
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean deleteUserById(Long userId, UserDto currentUser) throws ServiceException {
    logger.debug("Deletion attempt for user ID: {}", userId);
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      Role role = currentUser.getRole();
      boolean isAdmin = role.equals(Role.ROLE_ADMIN);
      Long currentUserId = currentUser.getId();
      boolean isCurrentUser = Objects.equals(currentUserId, userId);
      if (isAdmin || isCurrentUser) {
        return userDao.deleteById(userId);
      } else {
        throw new ServiceException("You dont have permission to perform this operation");
      }
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean updateUsername(Long id, String newUsername) throws ServiceException {
    logger.debug("Username update attempt for user ID: {} -> '{}'", id, newUsername);
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    try {
      return userDao.updateUsername(id, newUsername);
    } catch (DaoException e) {
      throw new ServiceException(e);
    }
  }
}