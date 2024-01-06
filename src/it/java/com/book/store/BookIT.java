package com.book.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.domain.BookEntity;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class BookIT extends AbstractIT {

  @Test
  void saveBookShouldRespondCreated() {
    buildRestAssuredWithTestToken().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "title": "Clean Code",
                      "author": "Robert C. Martin",
                      "price": 650.50,
                      "publication-year": 2020
                    }""")
            .post("/api/books")
            .then().assertThat().log().all()
            .statusCode(CREATED.value());

    assertThat(bookRepository.findAll()).hasSize(1)
            .flatExtracting(BookEntity::getTitle, BookEntity::getAuthor, BookEntity::getPrice, BookEntity::getPublicationYear)
            .containsExactly("Clean Code", "Robert C. Martin", BigDecimal.valueOf(650.50), 2020);
  }

  @Test
  void getBookByIdShouldRespondOk() {
    var book = saveBook("Clean Code", "Robert C. Martin", BigDecimal.valueOf(555.55), 2020);

    buildRestAssuredWithTestToken().when()
            .get("/api/books/%s".formatted(book.getId()))
            .then().assertThat().log().all()
            .statusCode(OK.value())
            .body("title", equalTo("Clean Code"))
            .body("author", equalTo("Robert C. Martin"))
            .body("publication-year", equalTo(2020))
            .body("price", equalTo(555.55f));
  }

  @Test
  void getBookByIdShouldRespondNotFount() {
    buildRestAssuredWithTestToken().when()
            .get("/api/books/unknown")
            .then().assertThat().log().all()
            .statusCode(NOT_FOUND.value())
            .body("error", equalTo("book-not-found"))
            .body("details", equalTo("Book wasn't found"));
  }

  @Test
  void getBooksShouldRespondOkWithEmptyList() {
    buildRestAssuredWithTestToken().when()
            .get("/api/books")
            .then().assertThat().log().all()
            .statusCode(OK.value())
            .body(equalTo("[]"));
  }

  @Test
  void getBooksShouldRespondOk() {
    saveBook("Clean Code", "Robert C. Martin", BigDecimal.valueOf(555.55), 2020);
    saveBook("Clean Architecture", "Robert C. Martin", BigDecimal.valueOf(600.00f), 2021);

    buildRestAssuredWithTestToken().when()
            .get("/api/books")
            .then().assertThat().log().all()
            .statusCode(OK.value())
            .body("size()", is(2))
            .body("[0].id", notNullValue())
            .body("[0].title", equalTo("Clean Code"))
            .body("[0].author", equalTo("Robert C. Martin"))
            .body("[0].publication-year", equalTo(2020))
            .body("[0].price", equalTo(555.55f))
            .body("[1].id", notNullValue())
            .body("[1].title", equalTo("Clean Architecture"))
            .body("[1].author", equalTo("Robert C. Martin"))
            .body("[1].publication-year", equalTo(2021))
            .body("[1].price", equalTo(600.00f));
  }

  @Test
  void deleteBookByIdShouldRespondOk() {
    var book = saveBook("Clean Code", "Robert C. Martin", BigDecimal.valueOf(555.55), 2020);

    buildRestAssuredWithTestToken().when()
            .delete("/api/books/%s".formatted(book.getId()))
            .then().assertThat().log().all()
            .statusCode(OK.value());
  }

  @Test
  void deleteBookByIdShouldRespondNotFount() {
    buildRestAssuredWithTestToken().when()
            .delete("/api/books/unknown")
            .then().assertThat().log().all()
            .statusCode(NOT_FOUND.value())
            .body("error", equalTo("book-not-found"))
            .body("details", equalTo("Book wasn't found"));
  }

  @Test
  void updateBookByIdShouldRespondNotFount() {
    buildRestAssuredWithTestToken().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "title": "Clean Code",
                      "author": "Robert C. Martin",
                      "price": 650.50,
                      "publication-year": 2020
                    }""")
            .put("/api/books/unknown")
            .then().assertThat().log().all()
            .statusCode(NOT_FOUND.value())
            .body("error", equalTo("book-not-found"))
            .body("details", equalTo("Book wasn't found"));
  }

  @Test
  void updateBookByIdShouldRespondOk() {
    var book = saveBook("Clean Architecture", "Robert C. Martin", BigDecimal.valueOf(600.00f), 2021);

    buildRestAssuredWithTestToken().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "title": "Clean Code",
                      "author": "Robert C. Martin",
                      "price": 650.50,
                      "publication-year": 2020
                    }""")
            .put("/api/books/%s".formatted(book.getId()))
            .then().assertThat().log().all()
            .statusCode(OK.value());

    assertThat(bookRepository.findById(book.getId())).isNotEmpty().get()
            .extracting(BookEntity::getTitle, BookEntity::getAuthor, BookEntity::getPrice, BookEntity::getPublicationYear)
            .containsExactly("Clean Code", "Robert C. Martin", BigDecimal.valueOf(650.50), 2020);
  }
}
