package com.book.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.domain.ReviewDocument;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class BookReviewIT extends AbstractIT {

  @Test
  void saveReviewShouldRespondCreated() {
    var book = saveBook("Clean Code", "Robert C. Martin", BigDecimal.valueOf(555.55), 2020);

    buildRestAssuredWithTestToken().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "comment": "great book!",
                      "rating": 5
                    }""")
            .post("/api/books/%s/reviews".formatted(book.getId()))
            .then().assertThat().log().all()
            .statusCode(CREATED.value());

    assertThat(reviewRepository.findAllByBookId(book.getId())).hasSize(1)
            .flatExtracting(ReviewDocument::getRating, ReviewDocument::getComment)
            .containsExactly( 5, "great book!");
  }

  @Test
  void saveReviewShouldRespondBookNotFound() {
    buildRestAssuredWithTestToken().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "comment": "great book!",
                      "rating": 5
                    }""")
            .post("/api/books/unknown/reviews")
            .then().assertThat().log().all()
            .statusCode(NOT_FOUND.value())
            .body("error", equalTo("book-not-found"))
            .body("details", equalTo("Book wasn't found"));
  }

  @Test
  void getReviewShouldRespondOk() {
    var book = saveBook("Clean Code", "Robert C. Martin", BigDecimal.valueOf(555.55), 2020);
    var review = saveReview(book.getId(), 5, "Great book!", "user_id");

    buildRestAssuredWithTestToken().when()
            .contentType(APPLICATION_JSON_VALUE)
            .get("/api/books/%s/reviews".formatted(book.getId()))
            .then().assertThat().log().all()
            .statusCode(OK.value())
            .body("size()", is(1))
            .body("[0].bookId", equalTo(review.getBookId()))
            .body("[0].rating", equalTo(5))
            .body("[0].comment", equalTo("Great book!"))
            .body("[0].timestamp", notNullValue())
            .body("[0].userId", equalTo("user_id"));
  }
}
