package com.innowise.web.service;

import com.innowise.web.dto.GoodDetailDto;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface GoodService {
  List<GoodDetailDto> findAllWithUsername() throws ServiceException;

  boolean add(Good good) throws ServiceException;

  boolean deleteById(Long id) throws ServiceException;

  List<GoodDto> findAllGoodDto() throws ServiceException;

  Optional<Good> findById(Long id) throws ServiceException;

  List<GoodDto> getGoodDtoListByUserId(Long userId) throws ServiceException;
}