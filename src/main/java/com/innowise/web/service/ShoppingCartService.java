package com.innowise.web.service;

import com.innowise.web.dto.ShoppingCartItemDto;
import com.innowise.web.exception.ServiceException;

import java.util.List;

public interface ShoppingCartService {
  boolean addItem(Long userId, Long goodId) throws ServiceException;

  List<ShoppingCartItemDto> findAllDtoByUserId(Long userId) throws ServiceException;

  boolean removeItem(Long userId, Long goodId) throws ServiceException;

  boolean order(Long userId, Long goodId, Long amount) throws ServiceException;
}