package com.book.store.auth;

import com.book.store.domain.UserEntity;
import com.book.store.exception.UserNotFoundException;
import com.book.store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final JwtHandler jwtHandler;
  private final UserRepository userRepository;

  @PostMapping("/api/login")
  public String login(@RequestBody LoginRequest request) {
    return userRepository.findByUsername(request.username())
            .filter(user -> user.getPassword().equals(request.password()))
            .map(UserEntity::getId)
            .map(jwtHandler::generateToken)
            .orElseThrow(UserNotFoundException::new);
  }

  public record LoginRequest(String username, String password) {
  }
}
