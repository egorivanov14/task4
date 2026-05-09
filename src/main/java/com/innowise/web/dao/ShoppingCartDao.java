package com.innowise.web.dao;

import com.innowise.web.dto.ShoppingCartItemDto;
import com.innowise.web.entity.ShoppingCartItem;
import com.innowise.web.exception.DaoException;
import com.innowise.web.exception.ServiceException;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ShoppingCartDao {
  boolean add(Connection connection, Long userId, Long goodId) throws ServiceException;

  boolean incrementQuantity(Connection connection, Long userId, Long goodId) throws ServiceException;

  boolean decrementQuantity(Connection connection, Long userId, Long goodId) throws ServiceException;

  Optional<ShoppingCartItem> getShoppingCartItem(Long userId, Long goodId) throws ServiceException;

  boolean exists(Long userId, Long goodId) throws ServiceException;

  List<ShoppingCartItemDto> findAllDtoByUserId(Long userId) throws DaoException;

  boolean deleteById(Long userId, Long goodId) throws DaoException;
}