package com.book.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.domain.BookEntity;
import com.book.store.domain.OrderEntity;
import com.book.store.domain.UserActivityLogDocument;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderIT extends AbstractIT {

  @Test
  void saveOrderShouldRespondCreated() {
    var book = saveBook("Clean Code", "Robert C. Martin", BigDecimal.valueOf(555.55), 2020);

    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    [
                      "%s"
                    ]""".formatted(book.getId()))
            .post("/api/users/orders")
            .then().assertThat().log().all()
            .statusCode(CREATED.value());

    assertThat(orderRepository.findAll()).hasSize(1).element(0)
            .hasFieldOrProperty("orderDate")
            .hasFieldOrProperty("user")
            .extracting(OrderEntity::getBooks)
            .asList()
            .hasSize(1)
            .element(0)
            .hasFieldOrPropertyWithValue("id", book.getId());
  }

  @Test
  void getUserOrderShouldRespondOk() {
    var user = saveUser("Joe", "password");
    var book = saveBook("Clean Code", "Robert C. Martin", BigDecimal.valueOf(555.55), 2020);
    var order = saveOrder(List.of(book), user);

    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .get("/api/users/orders")
            .then().assertThat().log().all()
            .statusCode(OK.value())
            .body("size()", is(1))
            .body("[0].book-ids.[0]", equalTo(book.getId()))
            .body("[0].timestamp", notNullValue());
  }
}
