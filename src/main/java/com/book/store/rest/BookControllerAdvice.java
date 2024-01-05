package com.book.store.rest;

import com.book.store.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = {BookController.class, BookReviewController.class})
public class BookControllerAdvice {

  @ExceptionHandler(BookNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleBookNotFound(BookNotFoundException exception) {
    log.debug(exception.getMessage());
    return new ErrorResponse("book-not-found", "Book wasn't found");
  }

  public record ErrorResponse(String error, String details) {
  }
}
