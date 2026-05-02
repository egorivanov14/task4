package com.innowise.web.dto;

import com.innowise.web.entity.Role;
import com.innowise.web.entity.User;

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
    int roleId = user.getRoleId();
    Role role = Role.defineRole(roleId);
    userDto.setRole(role);
    return userDto;
  }
}