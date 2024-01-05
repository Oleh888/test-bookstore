package com.book.store.rest.mapper;

import com.book.store.rest.BookController;
import com.book.store.service.BookService;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestBookMapper {

  BookService.BookDto toBookDto(BookController.SaveBookRequest saveBookRequest);

  BookController.BookResponse toBookResponse(BookService.BookDto bookDto);

  List<BookController.BookResponse> toBookResponse(List<BookService.BookDto> bookDto);

}
