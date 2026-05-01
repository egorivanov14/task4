package com.innowise.web.service;

import com.innowise.web.dto.GoodDto;
import com.innowise.web.entity.Good;
import com.innowise.web.exception.ServiceException;

import java.util.List;

public interface GoodService {
  List<Good> findAll() throws ServiceException;

  List<GoodDto> findAllDto() throws ServiceException;

  boolean add(Good good) throws ServiceException;
}