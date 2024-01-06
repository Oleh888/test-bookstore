package com.book.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.store.domain.ReviewDocument;
import com.book.store.exception.BookNotFoundException;
import com.book.store.mapper.BookReviewMapper;
import com.book.store.repository.BookRepository;
import com.book.store.repository.ReviewDocumentRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

class BookReviewServiceTest extends AbstractTest {

  @Mock
  private ReviewDocumentRepository reviewDocumentRepository;

  @Spy
  private BookReviewMapper bookReviewMapper = Mappers.getMapper(BookReviewMapper.class);

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookReviewService bookReviewService;

  @Test
  void shouldSaveReview() {
    var bookReview = getBookReviewDto();
    when(bookRepository.existsById("book_id")).thenReturn(true);
    when(reviewDocumentRepository.save(any(ReviewDocument.class))).thenReturn(new ReviewDocument());

    bookReviewService.saveReview(bookReview, "user_id");

    verify(bookRepository, times(1)).existsById("book_id");
    verify(reviewDocumentRepository, times(1)).save(any(ReviewDocument.class));
  }

  @Test
  void shouldThrowBookNotFoundDuringSaveReview() {
    var bookReview = getBookReviewDto();
    when(bookRepository.existsById("book_id")).thenReturn(false);

    assertThrows(BookNotFoundException.class, () -> bookReviewService.saveReview(bookReview, "user_id"));

    verify(bookRepository, times(1)).existsById("book_id");
    verify(reviewDocumentRepository, times(0)).save(any(ReviewDocument.class));
  }

  @Test
  void shouldReturnBookReviewsList() {
    when(bookRepository.existsById("book_id")).thenReturn(true);
    when(reviewDocumentRepository.findAllByBookId("book_id")).thenReturn(List.of(new ReviewDocument()));

    var bookReviews = bookReviewService.getBookReviews("book_id");

    assertThat(bookReviews).hasSize(1);
  }

  @Test
  void shouldThrowBookNotFoundDuringReturnBookReviewsList() {
    when(bookRepository.existsById("book_id")).thenReturn(false);

    assertThrows(BookNotFoundException.class, () -> bookReviewService.getBookReviews("book_id"));

    verify(reviewDocumentRepository, times(0)).findAllByBookId("book_id");
  }
}
