package com.innowise.web.service;

import com.innowise.web.dto.UserDto;
import com.innowise.web.entity.User;
import com.innowise.web.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface UserService {
  boolean register(String username, String password) throws ServiceException;

  boolean login(String username, String password) throws ServiceException;

  boolean setRole(String adminUsername, String toUsername, String role) throws ServiceException;

  Optional<User> getUser(String username) throws ServiceException;

  Optional<UserDto> getUserDto(String username) throws ServiceException;

  List<User> getUsers() throws ServiceException;

  List<UserDto> getUserDtoList() throws ServiceException;

  boolean deleteUserByUsername(String username) throws ServiceException;

  boolean updateUsername(Long id, String newUsername) throws ServiceException;
}