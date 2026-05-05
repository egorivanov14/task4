package com.innowise.web.dao;

import com.innowise.web.dto.GoodDetailDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface GoodDao {
  List<Good> findAllByAddedBy(Long userId) throws DaoException;

  List<GoodDetailDto> findAllWithUsername() throws DaoException;

  Optional<Good> findByAddedBy(Long addedBy) throws DaoException;
}