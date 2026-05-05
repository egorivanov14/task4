package com.innowise.web.service;

import com.innowise.web.entity.ShoppingCartItem;
import com.innowise.web.exception.ServiceException;

public interface ShoppingCartService {
  boolean add(ShoppingCartItem item) throws ServiceException;
}