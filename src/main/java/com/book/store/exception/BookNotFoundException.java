package com.book.store.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookNotFoundException extends RuntimeException {

  private final String missedBookId;

  @Override
  public String getMessage() {
    return "Book with id %s wasn't found".formatted(missedBookId);
  }
}
