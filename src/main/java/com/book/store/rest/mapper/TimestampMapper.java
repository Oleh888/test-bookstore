package com.book.store.rest.mapper;

import java.time.Instant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TimestampMapper {

  default Instant toInstant(Long timestamp) {
    return Instant.ofEpochMilli(timestamp);
  }

  default Long toEpochMilli(Instant timestamp) {
    return timestamp.toEpochMilli();
  }
}
