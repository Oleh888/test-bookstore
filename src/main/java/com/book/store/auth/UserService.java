package com.book.store.auth;

import com.book.store.domain.UserEntity;
import com.book.store.mapper.UserMapper;
import com.book.store.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserPasswordHandler passwordHandler;

  public Optional<UserDto> findByUsername(String username) {
    return userRepository.findByUsername(username)
            .map(userMapper::toUserDto);
  }

  public void saveUser(String username, String password) {
    var entity = new UserEntity();
    entity.setUsername(username);
    entity.setSalt(passwordHandler.generateRandomSalt());
    entity.setPassword(passwordHandler.hashPassword(password, entity.getSalt()));
    userRepository.save(entity);
  }

  public boolean checkUserPassword(String inputPassword, String userId) {
    return userRepository.findById(userId)
            .map(user -> user.getPassword().equals(passwordHandler.hashPassword(inputPassword, user.getSalt())))
            .orElse(false);
  }

  public record UserDto(String username, String password, String id) {
  }
}
