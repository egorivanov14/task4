package com.innowise.web.dao;

import com.innowise.web.exception.DaoException;

import java.sql.Connection;

public interface UserBalanceDao {
  boolean addEmpty(Connection connection, Long userId) throws DaoException;

  Long getBalanceByUserId(Long userId) throws DaoException;

  boolean updateBalanceByUserId(Long userId, Long amount) throws DaoException;

  boolean deductBalance(Connection connection, Long userId, Long amount) throws DaoException;
}