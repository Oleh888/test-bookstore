package com.book.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.store.auth.UserPasswordHandler;
import com.book.store.auth.UserService;
import com.book.store.domain.UserEntity;
import com.book.store.exception.PasswordHashingException;
import com.book.store.mapper.UserMapper;
import com.book.store.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

class UserServiceTest extends AbstractTest {

  @Spy
  private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserPasswordHandler userPasswordHandler;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldFindUser() {
    when(userRepository.findByUsername("username")).thenReturn(Optional.of(getUserEntity()));

    var user = userService.findByUsername("username");

    assertThat(user).isNotEmpty().get().extracting(UserService.UserDto::username, UserService.UserDto::id, UserService.UserDto::password)
            .containsExactly("username", "user_id", "password");
  }

  @Test
  void shouldNotFindUser() {
    when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

    var user = userService.findByUsername("username");

    assertThat(user).isEmpty();
  }

  @Test
  void shouldSaveUser() {
    byte[] salt = new byte[]{};
    when(userPasswordHandler.generateRandomSalt()).thenReturn(salt);
    when(userPasswordHandler.hashPassword("password", salt)).thenReturn("password");
    when(userRepository.save(any(UserEntity.class))).thenReturn(getUserEntity());

    userService.saveUser("username", "password");

    verify(userPasswordHandler, times(1)).generateRandomSalt();
    verify(userPasswordHandler, times(1)).hashPassword("password", salt);
    verify(userRepository, times(1)).save(any(UserEntity.class));
  }

  @Test
  void shouldThrowPasswordHashingExceptionWhenSaveUser() {
    byte[] salt = new byte[]{};
    when(userPasswordHandler.generateRandomSalt()).thenReturn(salt);
    when(userPasswordHandler.hashPassword("password", salt)).thenThrow(PasswordHashingException.class);

    assertThrows(PasswordHashingException.class, () ->userService.saveUser("username", "password"));

    verify(userPasswordHandler, times(1)).generateRandomSalt();
    verify(userPasswordHandler, times(1)).hashPassword("password", salt);
    verify(userRepository, times(0)).save(any(UserEntity.class));
  }
}
