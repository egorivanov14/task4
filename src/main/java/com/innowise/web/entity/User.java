package com.innowise.web.entity;

public class User {
  private Long id;
  private String username;
  private String password;
  private int roleId;

  public User() {
  }

  public User(Long id, String username, String password, int role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.roleId = role;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserName() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + " roleId=" + roleId + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (obj.getClass() == this.getClass()) {
      User objUser = (User) obj;
      return username.equals(objUser.username);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 17;
    hash += (username != null) ? username.hashCode() * 31 : 0;
    hash += (password != null) ? password.hashCode() : 0;
    return hash;
  }
}