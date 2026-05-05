package com.innowise.web.dto.converter;

import com.innowise.web.dto.GoodDto;
import com.innowise.web.entity.Good;

public class GoodConverter implements EntityConverter<Good, GoodDto> {

  @Override
  public GoodDto toDto(Good obj) {
    Long id = obj.getId();
    String name = obj.getName();
    Long price = obj.getPrice();
    Long quantity = obj.getQuantity();
    String description = obj.getDescription();
    String manufacturer = obj.getManufacturer();

    GoodDto goodDto = new GoodDto();
    goodDto.setId(id);
    goodDto.setName(name);
    goodDto.setPrice(price);
    goodDto.setQuantity(quantity);
    goodDto.setDescription(description);
    goodDto.setManufacturer(manufacturer);
    return goodDto;
  }
}