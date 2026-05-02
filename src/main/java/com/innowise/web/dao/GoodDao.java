package com.innowise.web.dao;

import com.innowise.web.dto.GoodDto;
import com.innowise.web.exception.DaoException;

import java.util.List;

public interface GoodDao {
  List<GoodDto> getGoodDtoListByUserId(Long userId) throws DaoException;

  List<GoodDto> findAllWithUsername() throws DaoException;
}