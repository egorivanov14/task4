package com.innowise.web.dao;

import com.innowise.web.dto.GoodDetailDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.DaoException;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GoodDao {
  List<Good> findAllByAddedBy(Long userId) throws DaoException;

  List<GoodDetailDto> findAllWithUsername() throws DaoException;

  Optional<Good> findByAddedBy(Long addedBy) throws DaoException;

  boolean reserveGood(Connection connection, Long userId, Long goodId) throws DaoException;

  boolean incrementQuantity(Connection connection, Long goodId) throws DaoException;

  List<Good> findAllAvailable() throws DaoException;
}