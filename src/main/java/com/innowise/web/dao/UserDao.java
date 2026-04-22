package com.innowise.web.dao;

import com.innowise.web.entity.User;
import com.innowise.web.exception.DaoException;

import java.util.Optional;

public interface UserDao {
  Optional<User> findByUsername(String username) throws DaoException;

  boolean existsByUsername(String username) throws DaoException;
}