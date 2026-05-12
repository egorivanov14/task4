package com.innowise.web.entity;

public class Balance {
  private Long id;
  private Long userId;
  private Long balance;

  public Balance() {
  }

  public Balance(Long id, Long userId, Long balance) {
    this.id = id;
    this.userId = userId;
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getBalance() {
    return balance;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setBalance(Long balance) {
    this.balance = balance;
  }
}