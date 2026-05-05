package com.innowise.web.dto.converter;

public interface EntityConverter<S, T> {
  T toDto(S obj);
}