package com.book.store.graphql;

import com.book.store.graphql.mapper.GraphqlBookMapper;
import com.book.store.service.BookService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GraphqlBookController {

  private final BookService bookService;
  private final GraphqlBookMapper bookMapper;

  @QueryMapping
  public Book bookById(@Argument String id) {
    return bookMapper.toBookResponse(bookService.getBookById(id));
  }

  @QueryMapping
  public List<Book> bookList() {
    return bookMapper.toBooksResponse(bookService.getAllBooks());
  }

  public record Book(String id, String title, String author, BigDecimal price, Integer publicationYear) {
  }
}
