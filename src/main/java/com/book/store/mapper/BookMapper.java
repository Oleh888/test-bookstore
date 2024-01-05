package com.book.store.mapper;

import com.book.store.domain.BookEntity;
import com.book.store.service.BookService;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {

  BookEntity toBookEntity(BookService.BookDto bookDto);

  @Mapping(target = "id", source = "id")
  BookEntity toBookEntity(@MappingTarget BookEntity bookEntity, BookService.BookDto bookDto, String id);

  BookService.BookDto toBookDto(BookEntity bookEntity);

  List<BookService.BookDto> toBookDto(List<BookEntity> books);

}
