package com.book.store.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.rest.mapper.RestOrderMapper;
import com.book.store.service.OrderService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users/orders")
public class OrderController {

  @Qualifier("user_id")
  private final Supplier<String> userIdSupplier;
  private final RestOrderMapper orderMapper;
  private final OrderService orderService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void saveOrder(@RequestBody List<String> bookIds) {
    log.debug("Received save order request: {}", bookIds);

    orderService.saveOrder(orderMapper.toOrderDto(bookIds), userIdSupplier.get());
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public List<OrderResponse> getUserActivity() {
    return orderMapper.toOrdersResponse(orderService.getAllOrders(userIdSupplier.get()));
  }

  public record OrderResponse(@JsonProperty("book-ids") List<String> bookIds, Long timestamp) {
  }
}
