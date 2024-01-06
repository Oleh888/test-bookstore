package com.book.store.graphql.mapper;

import com.book.store.graphql.GraphqlBookController;
import com.book.store.service.BookService;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GraphqlBookMapper {

  GraphqlBookController.Book toBookResponse(BookService.BookDto bookDto);

  List<GraphqlBookController.Book> toBooksResponse(List<BookService.BookDto> booksDto);

}
