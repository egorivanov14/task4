package com.innowise.web.dto;

public class GoodDto {
  private Long id;
  private String name;
  private Long price;
  private Long quantity;
  private String description;
  private String manufacturer;
  private String addedByUsername;

  public GoodDto() {}

  public GoodDto(Long id, String name, Long price, Long quantity, String description, String manufacturer, String addedByUsername) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.description = description;
    this.manufacturer = manufacturer;
    this.addedByUsername = addedByUsername;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Long getPrice() {
    return price;
  }

  public Long getQuantity() {
    return quantity;
  }

  public String getDescription() {
    return description;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public String getAddedByUsername() {
    return addedByUsername;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public void setAddedByUsername(String addedByUsername) {
    this.addedByUsername = addedByUsername;
  }
}