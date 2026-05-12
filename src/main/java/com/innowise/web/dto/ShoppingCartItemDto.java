package com.innowise.web.dto;

public class ShoppingCartItemDto {
  private Long goodId;
  private String goodName;
  private Long quantity;
  private Long amount;

  public ShoppingCartItemDto() {}

  public ShoppingCartItemDto(Long goodId, String goodName, Long quantity,  Long amount) {
    this.goodId = goodId;
    this.goodName = goodName;
    this.quantity = quantity;
    this.amount = amount;
  }

  public Long getGoodId() {
    return goodId;
  }

  public String getGoodName() {
    return goodName;
  }

  public Long getQuantity() {
    return quantity;
  }

  public Long getAmount() {
    return amount;
  }

  public void setGoodId(Long goodId) {
    this.goodId = goodId;
  }

  public void setGoodName(String goodName) {
    this.goodName = goodName;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }
}