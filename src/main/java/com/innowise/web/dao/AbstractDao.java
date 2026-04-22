package com.innowise.web.dao;

import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;

import java.util.List;

public abstract class AbstractDao<T> {
  public abstract boolean add(T t) throws DaoException;

  public abstract boolean update(T t) throws DaoException;

  public abstract boolean delete(T t) throws DaoException;

  public abstract List<User> findAll() throws DaoException;
}