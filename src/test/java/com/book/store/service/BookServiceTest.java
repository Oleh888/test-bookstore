package com.book.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.store.domain.BookEntity;
import com.book.store.exception.BookNotFoundException;
import com.book.store.mapper.BookMapper;
import com.book.store.repository.BookRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

class BookServiceTest extends AbstractTest {

  @Spy
  private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookService bookService;

  @Test
  void shouldReturnBookById() {
    when(bookRepository.findById("book_id")).thenReturn(Optional.of(getBookEntity()));

    var book = bookService.getBookById("book_id");

    assertThat(book).extracting(BookService.BookDto::author, BookService.BookDto::title, BookService.BookDto::price, BookService.BookDto::publicationYear, BookService.BookDto::id)
            .containsExactly("Robert C. Martin", "Clean Code", BigDecimal.TEN, 2003, "book_id");
  }

  @Test
  void shouldThrowBookNotFoundWhenReturnBookById() {
    when(bookRepository.findById("book_id")).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> bookService.getBookById("book_id"));
  }

  @Test
  void shouldSaveBook() {
    when(bookRepository.save(any(BookEntity.class))).thenReturn(getBookEntity());

    bookService.saveBook(getBookDto());

    verify(bookRepository, times(1)).save(any(BookEntity.class));
  }

  @Test
  void shouldReturnAllBooks() {
    when(bookRepository.findAll()).thenReturn(List.of(getBookEntity()));

    var books = bookService.getAllBooks();

    assertThat(books).hasSize(1).flatExtracting(BookService.BookDto::author, BookService.BookDto::title, BookService.BookDto::price, BookService.BookDto::publicationYear, BookService.BookDto::id)
            .containsExactly("Robert C. Martin", "Clean Code", BigDecimal.TEN, 2003, "book_id");
  }

  @Test
  void shouldDeleteBook() {
    when(bookRepository.existsById("book_id")).thenReturn(true);

    bookService.deleteBook("book_id");

    verify(bookRepository, times(1)).deleteById("book_id");
  }

  @Test
  void shouldThrowBookNotFoundWhenDeleteBook() {
    when(bookRepository.existsById("book_id")).thenReturn(false);

    assertThrows(BookNotFoundException.class, () -> bookService.deleteBook("book_id"));

    verify(bookRepository, times(0)).deleteById("book_id");
  }

  @Test
  void shouldUpdateBook() {
    when(bookRepository.findById("book_id")).thenReturn(Optional.of(getBookEntity()));
    when(bookRepository.save(any(BookEntity.class))).thenReturn(getBookEntity());

    bookService.updateBook(getBookDto(), "book_id");

    verify(bookRepository, times(1)).findById("book_id");
    verify(bookRepository, times(1)).save(any(BookEntity.class));
  }

  @Test
  void shouldThrowBookNotFoundWhenUpdateBook() {
    when(bookRepository.findById("book_id")).thenReturn(Optional.empty());

    assertThrows(BookNotFoundException.class, () -> bookService.updateBook(getBookDto(), "book_id"));

    verify(bookRepository, times(1)).findById("book_id");
    verify(bookRepository, times(0)).save(any(BookEntity.class));
  }
}
