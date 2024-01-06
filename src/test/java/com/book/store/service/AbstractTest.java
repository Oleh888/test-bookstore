package com.book.store.service;

import com.book.store.domain.BookEntity;
import com.book.store.domain.OrderEntity;
import com.book.store.domain.UserActivityLogDocument;
import com.book.store.domain.UserEntity;
import com.book.store.rest.mapper.TimestampMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractTest {

  @Spy
  protected TimestampMapper timestampMapper = Mappers.getMapper(TimestampMapper.class);

  protected BookReviewService.BookReviewDto getBookReviewDto() {
    return new BookReviewService.BookReviewDto("book_id", 5, "cool book", null, null);
  }

  protected BookService.BookDto getBookDto() {
    return new BookService.BookDto("book_id", "Clean Code", "Robert C. Martin", BigDecimal.TEN, 2003);
  }

  protected BookEntity getBookEntity() {
    var entity = new BookEntity();
    entity.setTitle("Clean Code");
    entity.setPublicationYear(2003);
    entity.setPrice(BigDecimal.TEN);
    entity.setAuthor("Robert C. Martin");
    entity.setId("book_id");
    return entity;
  }

  protected OrderService.OrderDto getOrderDto() {
    return new OrderService.OrderDto("order_id", Instant.now().toEpochMilli(), List.of("book_id"));
  }

  protected UserEntity getUserEntity() {
    var entity = new UserEntity();
    entity.setSalt(new byte[]{});
    entity.setUsername("username");
    entity.setPassword("password");
    entity.setId("user_id");
    return entity;
  }

  protected OrderEntity getOrderEntity() {
    var entity = new OrderEntity();
    entity.setUser(getUserEntity());
    entity.setBooks(List.of(getBookEntity()));
    entity.setOrderDate(Instant.now());
    entity.setId("order_id");
    return entity;
  }

  protected UserActivityLogDocument getUserActivityLogDocument() {
    var document = new UserActivityLogDocument();
    document.setUserId("user_id");
    document.setActivityLogs(new ArrayList<>(List.of(getActivityLog())));
    return document;
  }

  protected UserActivityLogDocument.ActivityLog getActivityLog() {
    var activityLog = new UserActivityLogDocument.ActivityLog();
    activityLog.setDetails("User visited home page");
    activityLog.setTimestamp(Instant.now());
    activityLog.setActivityType("visit");
    return activityLog;
  }

  protected UserActivityService.UserActivityLogDto getUserActivityLogDto() {
    return new UserActivityService.UserActivityLogDto("visit", "User visited home page", Instant.now());
  }
}
