package com.innowise.web.entity;

public class Good {
  private Long id;
  private String name;
  private Long price;
  private Long quantity;
  private String description;
  private String manufacturer;

  public Good() {
  }

  public Good(Long id, String name, Long price, Long quantity, String manufacturer, String description) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.manufacturer = manufacturer;
    this.description = description;
  }

  public Good(String name, Long price, Long quantity, String manufacturer, String description) {
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.manufacturer = manufacturer;
    this.description = description;
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

  public String getManufacturer() {
    return manufacturer;
  }

  public String getDescription() {
    return description;
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

  public void setDescription(String description) {
    this.description = description;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }
}