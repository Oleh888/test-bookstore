package com.book.store.mapper;

import com.book.store.domain.BookEntity;
import com.book.store.domain.OrderEntity;
import com.book.store.domain.UserEntity;
import com.book.store.service.OrderService;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  OrderEntity toOrderEntity(List<BookEntity> books, UserEntity user);

  OrderService.OrderDto toOrderDto(OrderEntity entity);

  List<OrderService.OrderDto> toOrderDto(List<OrderEntity> orders);
}
