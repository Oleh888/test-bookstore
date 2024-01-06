package com.book.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.store.domain.OrderEntity;
import com.book.store.exception.UserNotFoundException;
import com.book.store.mapper.OrderMapper;
import com.book.store.repository.BookRepository;
import com.book.store.repository.OrderRepository;
import com.book.store.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

class OrderServiceTest extends AbstractTest {

  @Spy
  @InjectMocks
  private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private BookRepository bookRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private OrderService orderService;

  @Test
  void shouldSaveOrder() {
    when(orderRepository.save(any(OrderEntity.class))).thenReturn(getOrderEntity());
    when(bookRepository.findAllById(List.of("book_id"))).thenReturn(List.of(getBookEntity()));
    when(userRepository.findById("user_id")).thenReturn(Optional.of(getUserEntity()));

    orderService.saveOrder(getOrderDto(), "user_id");

    verify(orderRepository, times(1)).save(any(OrderEntity.class));
    verify(bookRepository, times(1)).findAllById(List.of("book_id"));
    verify(userRepository, times(1)).findById("user_id");
  }

  @Test
  void shouldThrowUserNotFoundWhenSaveOrder() {
    when(bookRepository.findAllById(List.of("book_id"))).thenReturn(List.of(getBookEntity()));
    when(userRepository.findById("user_id")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> orderService.saveOrder(getOrderDto(), "user_id"));

    verify(orderRepository, times(0)).save(any(OrderEntity.class));
    verify(bookRepository, times(1)).findAllById(List.of("book_id"));
    verify(userRepository, times(1)).findById("user_id");
  }

  @Test
  void shouldReturnAllOrders() {
    when(orderRepository.findAllByUserId("user_id")).thenReturn(List.of(getOrderEntity()));

    var orders = orderService.getAllOrders("user_id");

    assertThat(orders).hasSize(1);
  }
}
