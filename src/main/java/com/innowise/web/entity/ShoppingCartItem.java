package com.innowise.web.entity;

public class ShoppingCartItem {
  private Long userId;
  private Long goodId;
  private Long quantity;

  public ShoppingCartItem() {}

  public ShoppingCartItem(Long userId, Long goodId, Long quantity) {
    this.userId = userId;
    this.goodId = goodId;
    this.quantity = quantity;
  }

  public Long getUserId() {
    return userId;
  }

  public Long getGoodId() {
    return goodId;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public void setGoodId(Long goodId) {
    this.goodId = goodId;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }
}