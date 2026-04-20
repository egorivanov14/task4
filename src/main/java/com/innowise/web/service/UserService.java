package com.innowise.web.service;

import com.innowise.web.exception.ServiceException;

public interface UserService {
    boolean register(String username, String password) throws ServiceException;

    boolean login(String username, String password) throws ServiceException;
}