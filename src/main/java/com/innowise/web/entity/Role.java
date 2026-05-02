package com.innowise.web.entity;

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

  public static Role defineRole(int roleId) {
    for(Role role : Role.values()) {
      if(role.roleId == roleId) {
        return role;
      }
    }
    return Role.ROLE_USER;
  }

  public int getRoleId() {
    return roleId;
  }
}