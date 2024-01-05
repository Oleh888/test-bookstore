package com.book.store.rest.mapper;

import com.book.store.rest.OrderController;
import com.book.store.service.OrderService;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TimestampMapper.class)
public interface RestOrderMapper {

  default OrderService.OrderDto toOrderDto(List<String> bookIds) {
    return new OrderService.OrderDto(null, null, bookIds);
  }

  OrderController.OrderResponse toOrderResponse(OrderService.OrderDto orderDto);

  List<OrderController.OrderResponse> toOrdersResponse(List<OrderService.OrderDto> ordersDto);
}
