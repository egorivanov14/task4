package com.innowise.web.dto;

import com.innowise.web.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Converter {
  private static Converter instance;

  private Converter() {}

  public static Converter getInstance() {
    if (instance == null) {
      instance = new Converter();
    }
    return instance;
  }

  public UserDto convertUserToDto(User user) {
    UserDto userDto = new UserDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUserName());
    userDto.setRole(user.getRole());
    return userDto;
  }
}