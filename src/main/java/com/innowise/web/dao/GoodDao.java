package com.innowise.web.dao;

import com.innowise.web.dto.GoodDetailDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.DaoException;

import java.sql.Connection;
import java.util.List;

public interface GoodDao {
  List<Good> findAllByAddedBy(Long userId) throws DaoException;

  List<GoodDetailDto> findAllWithUsername() throws DaoException;

  boolean decrementQuantity(Connection connection, Long userId, Long goodId) throws DaoException;

  boolean incrementQuantity(Connection connection, Long goodId) throws DaoException;

  boolean changeQuantity(Long goodId, Long quantity) throws DaoException;

  List<Good> findAllAvailable() throws DaoException;
}