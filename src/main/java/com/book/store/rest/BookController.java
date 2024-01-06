package com.book.store.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.rest.mapper.RestBookMapper;
import com.book.store.service.BookService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/books")
public class BookController {

  private final RestBookMapper bookMapper;
  private final BookService bookService;

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public List<BookResponse> getBooks() {
    return bookMapper.toBookResponse(bookService.getAllBooks());
  }

  @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
  public BookResponse getBook(@PathVariable String id) {
    return bookMapper.toBookResponse(bookService.getBookById(id));
  }

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public BookIdentifier saveBook(@RequestBody SaveBookRequest request) {
    log.debug("Received save book request: {}", request);
    return new BookIdentifier(bookService.saveBook(bookMapper.toBookDto(request)).id());
  }

  @DeleteMapping(value = "{id}")
  public void deleteBook(@PathVariable String id) {
    bookService.deleteBook(id);
  }

  @PutMapping(value = "{id}", consumes = APPLICATION_JSON_VALUE)
  public void updateBook(@PathVariable String id, @RequestBody SaveBookRequest request) {
    log.debug("Received update book request: {}", request);
    bookService.updateBook(bookMapper.toBookDto(request), id);
  }

  public record BookResponse(String id, String title, String author, BigDecimal price,
                             @JsonProperty("publication-year") Integer publicationYear) {
  }

  public record SaveBookRequest(String title, String author, BigDecimal price,
                                @JsonProperty("publication-year") Integer publicationYear) {
  }

  public record BookIdentifier(String id) {
  }
}
