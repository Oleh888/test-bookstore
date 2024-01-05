package com.book.store.service;

import com.book.store.exception.BookNotFoundException;
import com.book.store.mapper.BookMapper;
import com.book.store.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

  private final BookRepository bookRepository;
  private final BookMapper bookMapper;

  public void saveBook(BookDto bookDto) {
    bookRepository.save(bookMapper.toBookEntity(bookDto));
  }

  public BookDto getBookById(String id) {
    return bookRepository.findById(id)
            .map(bookMapper::toBookDto)
            .orElseThrow(() -> new BookNotFoundException(id));
  }

  public List<BookDto> getAllBooks() {
    return bookMapper.toBookDto(bookRepository.findAll());
  }

  public void deleteBook(String id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
    } else {
      throw new BookNotFoundException(id);
    }
  }

  public void updateBook(BookDto bookDto, String id) {
    bookRepository.findById(id).ifPresentOrElse(entity ->
            bookRepository.save(bookMapper.toBookEntity(entity, bookDto, id)), () -> {
      throw new BookNotFoundException(id);
    });
  }

  public record BookDto(String id, String title, String author, BigDecimal price, Integer publicationYear) {
  }
}
