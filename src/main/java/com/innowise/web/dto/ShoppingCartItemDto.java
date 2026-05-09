package com.innowise.web.dto;

public class ShoppingCartItemDto {
  private Long goodId;
  private String goodName;
  private Long quantity;
  private Long fullPrice;

  public ShoppingCartItemDto() {}

  public ShoppingCartItemDto(Long goodId, String goodName, Long quantity,  Long fullPrice) {
    this.goodId = goodId;
    this.goodName = goodName;
    this.quantity = quantity;
    this.fullPrice = fullPrice;
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

  public Long getFullPrice() {
    return fullPrice;
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

  public void setFullPrice(Long fullPrice) {
    this.fullPrice = fullPrice;
  }
}