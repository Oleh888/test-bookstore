package com.book.store.mapper;

import com.book.store.auth.UserService;
import com.book.store.domain.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserService.UserDto toUserDto(UserEntity entity);

}
