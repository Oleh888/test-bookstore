package com.book.store.auth;

import com.book.store.exception.AuthenticationException;
import com.book.store.exception.NotUniqueUsernameException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final JwtHandler jwtHandler;
  private final UserService userService;

  @PostMapping("/api/login")
  public AccessTokenResponse login(@RequestBody LoginRequest request) {
    return userService.findByUsername(request.username())
            .filter(user -> userService.checkUserPassword(request.password(), user.id()))
            .map(UserService.UserDto::id)
            .map(jwtHandler::generateToken)
            .map(AccessTokenResponse::new)
            .orElseThrow(() -> new AuthenticationException("Username or password wasn't correct"));
  }

  @PostMapping("/api/users/create")
  @ResponseStatus(HttpStatus.CREATED)
  public void createUser(@RequestBody SaveUserRequest request) {
    if (userService.findByUsername(request.username()).isPresent()) {
      throw new NotUniqueUsernameException();
    }
    userService.saveUser(request.username(), request.password());
  }

  public record LoginRequest(String username, String password) {
  }

  public record SaveUserRequest(String username, String password) {
  }

  public record AccessTokenResponse(String token) {
  }
}
