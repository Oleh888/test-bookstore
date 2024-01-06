package com.book.store.auth;

import com.book.store.exception.AuthenticationException;
import com.book.store.exception.NotUniqueUsernameException;
import com.book.store.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthAdvice {

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleUserNotFound(UserNotFoundException exception) {
    log.debug(exception.getMessage());
    return new ErrorResponse("user-not-found", "User wasn't found");
  }

  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleAuthentication(AuthenticationException exception) {
    log.debug(exception.getMessage());
    return new ErrorResponse("not-authenticate", exception.getDetails());
  }

  @ExceptionHandler(NotUniqueUsernameException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleNotUniqueUsername() {
    log.debug("Couldn't create user with not unique username");
    return new ErrorResponse("not-unique-username", "Couldn't create user with not unique username");
  }

  public record ErrorResponse(String error, String details) {
  }
}
