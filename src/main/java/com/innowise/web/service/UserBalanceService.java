package com.innowise.web.service;

import com.innowise.web.exception.ServiceException;

public interface UserBalanceService {
  Long getBalanceByUserId(Long userId) throws ServiceException;

  boolean uppBalance(Long userId, Long amount) throws ServiceException;
}