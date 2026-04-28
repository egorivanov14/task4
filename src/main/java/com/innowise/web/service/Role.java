package com.innowise.web.service;

public enum Role {
  ROLE_USER(1),
  ROLE_ADMIN(2);

  private final int roleId;

  Role(int roleId) {
    this.roleId = roleId;
  }

  public static int defineRole(String roleStr) {
    Role role = Role.valueOf(roleStr.toUpperCase());
    return role.roleId;
  }

  public int getRoleId() {
    return roleId;
  }
}