package com.innowise.web.service;

import com.innowise.web.dto.GoodDetailDto;
import com.innowise.web.dto.GoodDto;
import com.innowise.web.dto.UserDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface GoodService {
  List<GoodDetailDto> getGoodDtoListWithUsername(Long adminId) throws ServiceException;

  boolean add(Good good) throws ServiceException;

  boolean deleteById(Long goodId, UserDto currentUser) throws ServiceException;

  List<GoodDto> getAvailableGoodDtoList() throws ServiceException;

  List<GoodDto> getGoodDtoListByUserId(Long userId) throws ServiceException;

  boolean changeQuantity(Long userId, Long goodId, Long newQuantity) throws ServiceException;
}