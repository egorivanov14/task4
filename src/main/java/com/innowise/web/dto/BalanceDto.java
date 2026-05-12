package com.innowise.web.dto;

public class BalanceDto {
  private Long userId;
  private Long balance;

  public BalanceDto() {
  }

  public BalanceDto(Long userId, Long balance) {
    this.userId = userId;
    this.balance = balance;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getBalance() {
    return balance;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setBalance(Long balance) {
    this.balance = balance;
  }
}