package com.innowise.web.entity;

public class ShoppingCartItem {
  private Long userId;
  private Long goodId;
  private Long quantity;
  private String description;

  public ShoppingCartItem() {}

  public ShoppingCartItem(Long userId, Long goodId, Long quantity, String description) {
    this.userId = userId;
    this.goodId = goodId;
    this.quantity = quantity;
    this.description = description;
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

  public String getDescription() {
    return description;
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

  public void setDescription(String description) {
    this.description = description;
  }
}