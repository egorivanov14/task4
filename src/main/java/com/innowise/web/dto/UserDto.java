package com.innowise.web.dto;

import com.innowise.web.service.Role;

public class UserDto {
  Long id;
  String username;
  Role role;

  public void setId(Long id) {
    this.id = id;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }

  public Long getId() {
    return id;
  }

  public Role getRole() {
    return role;
  }

  public String getRoleName() {
    return role.toString();
  }

  @Override
  public String toString() {
    return "UserDto [id=" + id + ", username=" + username + ", role=" + role + "]";
  }
}