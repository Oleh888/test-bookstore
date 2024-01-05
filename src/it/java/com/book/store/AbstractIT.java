package com.book.store;

import com.book.store.auth.JwtHandler;
import com.book.store.domain.BookEntity;
import com.book.store.domain.OrderEntity;
import com.book.store.domain.ReviewDocument;
import com.book.store.domain.UserActivityLogDocument;
import com.book.store.domain.UserEntity;
import com.book.store.repository.BookRepository;
import com.book.store.repository.OrderRepository;
import com.book.store.repository.ReviewDocumentRepository;
import com.book.store.repository.UserActivityLogDocumentRepository;
import com.book.store.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

@ActiveProfiles("it")
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIT {

  @LocalServerPort
  private int port;

  @Autowired
  protected BookRepository bookRepository;

  @Autowired
  protected ReviewDocumentRepository reviewRepository;

  @Autowired
  protected UserActivityLogDocumentRepository userActivityLogRepository;

  @Autowired
  protected OrderRepository orderRepository;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected JwtHandler jwtHandler;

  @BeforeEach
  void reset() {
    orderRepository.deleteAll();
    userRepository.deleteAll();
    bookRepository.deleteAll();
    reviewRepository.deleteAll();
    userActivityLogRepository.deleteAll();
  }

  protected RequestSpecification buildRestAssured() {
    return RestAssured.given().port(port);
  }

  protected RequestSpecification buildRestAssuredWithTestToken() {
    return RestAssured.given().port(port)
            .header("X-ACCESS-TOKEN", generateAccessTokenForUser(saveUser("Bob", "password")));
  }

  protected RequestSpecification buildRestAssuredWithTestToken(UserEntity user) {
    return RestAssured.given().port(port).header("X-ACCESS-TOKEN", generateAccessTokenForUser(user));
  }

  protected BookEntity saveBook(String title, String author, BigDecimal price, Integer publicationYear) {
    var entity = new BookEntity();
    entity.setTitle(title);
    entity.setAuthor(author);
    entity.setPrice(price);
    entity.setPublicationYear(publicationYear);
    return bookRepository.saveAndFlush(entity);
  }

  protected ReviewDocument saveReview(String bookId, Integer rating, String comment, String userId) {
    var document = new ReviewDocument();
    document.setRating(rating);
    document.setComment(comment);
    document.setBookId(bookId);
    document.setUserId(userId);
    return reviewRepository.save(document);
  }

  protected UserActivityLogDocument saveUserActivityLog(String userId, String details, String activityType) {
    var document = new UserActivityLogDocument();
    var activityLog = new UserActivityLogDocument.ActivityLog();
    activityLog.setActivityType(activityType);
    activityLog.setDetails(details);
    document.setActivityLogs(List.of(activityLog));
    document.setUserId(userId);
    return userActivityLogRepository.save(document);
  }

  protected OrderEntity saveOrder(List<BookEntity> books, UserEntity user) {
    var order = new OrderEntity();
    order.setBooks(books);
    order.setUser(user);
    return orderRepository.save(order);
  }

  protected UserEntity saveUser(String usernmae, String password) {
    var user = new UserEntity();
    user.setUsername(usernmae);
    user.setPassword(password);
    return userRepository.save(user);
  }

  protected String generateAccessTokenForUser(UserEntity user) {
    return jwtHandler.generateToken(user.getId());
  }
}
