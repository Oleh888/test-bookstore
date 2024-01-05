package com.book.store.service;

import com.book.store.exception.UserNotFoundException;
import com.book.store.mapper.OrderMapper;
import com.book.store.repository.BookRepository;
import com.book.store.repository.OrderRepository;
import com.book.store.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  public void saveOrder(OrderDto orderDto, String userId) {
    orderRepository.save(orderMapper.toOrderEntity(
            bookRepository.findAllById(orderDto.bookIds()),
            userRepository.findById(userId).orElseThrow(UserNotFoundException::new))
    );
  }

  public List<OrderDto> getAllOrders(String userId) {
    return orderMapper.toOrderDto(orderRepository.findAllByUserId(userId));
  }

  public record OrderDto(String orderId, Long timestamp, List<String> bookIds) {
  }
}
