package com.innowise.web.servise.impl;

import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.entity.User;
import com.innowise.web.servise.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class.getName());
    public static UserServiceImpl instance;
    private static final Lock lock = new ReentrantLock();

    private UserServiceImpl() {
    }

    public static UserServiceImpl getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new UserServiceImpl();
                    logger.info("UserServiceImpl instance has been created.");
                }
            } finally {
                lock.unlock();
            }
        }
        logger.info("UserServiceImpl instance ");
        return instance;
    }

    @Override
    public boolean register(String username, String password) {
        logger.info("UserServiceImpl trying to register user: {}", username);
        UserDaoImpl userDao = UserDaoImpl.getInstance();
        boolean isUserExists = userDao.existsByUsername(username);
        boolean result = false;

        if (!isUserExists) {
            logger.info("UserServiceImpl register user: {}", username);
            User user = new User(username, password);
            result = userDao.add(user);
        }
        return result;
    }

    @Override
    public boolean login(String username, String password) {
        logger.info("UserServiceImpl trying to login user: {}", username);
        UserDaoImpl userDao = UserDaoImpl.getInstance();
        Optional<User> userOptional = userDao.findByUsername(username);
        boolean result = false;

        if (userOptional.isPresent()) {
            logger.info("UserServiceImpl login user: {}", username);
            User user = userOptional.get();
            result = user.getPassword().equals(password); //todo
        }
        return result;
    }
}