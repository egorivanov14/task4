package com.innowise.web.dao;

import com.innowise.web.exception.DaoException;

import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T> {
  public abstract boolean add(T t) throws DaoException;

  public abstract boolean update(T t) throws DaoException;

  public abstract boolean delete(T t) throws DaoException;

  public abstract boolean deleteById(Long id) throws DaoException;

  public abstract List<T> findAll() throws DaoException;

  public abstract Optional<T> findById(Long id) throws DaoException;

  public abstract boolean existsById(Long id) throws DaoException;
}