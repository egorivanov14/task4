package com.innowise.web.dto.converter;

import com.innowise.web.dto.UserDto;
import com.innowise.web.entity.Role;
import com.innowise.web.entity.User;

public class UserConverter implements EntityConverter<User, UserDto> {
  @Override
  public UserDto toDto(User user) {
    Long id = user.getId();
    String username = user.getUserName();
    int roleId = user.getRoleId();
    Role role = Role.defineRole(roleId);

    UserDto userDto = new UserDto();
    userDto.setId(id);
    userDto.setUsername(username);
    userDto.setRole(role);
    return userDto;
  }
}