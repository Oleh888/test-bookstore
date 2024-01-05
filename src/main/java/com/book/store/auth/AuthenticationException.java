package com.book.store.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AuthenticationException extends RuntimeException {

  private final String userId;

  @Override
  public String getMessage() {
    return "User with id %s wasn't found".formatted(userId);
  }
}
