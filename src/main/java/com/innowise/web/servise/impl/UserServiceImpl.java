package com.innowise.web.servise.impl;

import com.innowise.web.dao.UserDao;
import com.innowise.web.dao.impl.UserDaoImpl;
import com.innowise.web.entity.User;
import com.innowise.web.servise.UserService;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UserServiceImpl implements UserService {
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
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    @Override
    public boolean register(String username, String password) {
        UserDao userDao = new UserDaoImpl();
        User user = new User(username, password);

        return userDao.add(user);
    }
}