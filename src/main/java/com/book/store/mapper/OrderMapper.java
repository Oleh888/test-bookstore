package com.book.store.mapper;

import com.book.store.domain.BookEntity;
import com.book.store.domain.OrderEntity;
import com.book.store.domain.UserEntity;
import com.book.store.rest.mapper.TimestampMapper;
import com.book.store.service.OrderService;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TimestampMapper.class)
public interface OrderMapper {

  @Mapping(target = "user", source = "user")
  OrderEntity toOrderEntity(List<BookEntity> books, UserEntity user);

  @Mapping(target = "timestamp", source = "orderDate")
  @Mapping(target = "bookIds", expression = "java(toBooksId(entity))")
  @Mapping(target = "orderId", source = "id")
  OrderService.OrderDto toOrderDto(OrderEntity entity);

  default List<String> toBooksId(OrderEntity entity) {
    return entity.getBooks().stream()
            .map(BookEntity::getId)
            .toList();
  }

  List<OrderService.OrderDto> toOrderDto(List<OrderEntity> orders);
}
