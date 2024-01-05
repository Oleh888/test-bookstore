package com.book.store.service;

import com.book.store.exception.BookNotFoundException;
import com.book.store.mapper.BookReviewMapper;
import com.book.store.repository.BookRepository;
import com.book.store.repository.ReviewDocumentRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewService {

  private final ReviewDocumentRepository reviewDocumentRepository;
  private final BookReviewMapper bookReviewMapper;
  private final BookRepository bookRepository;

  public void saveReview(BookReviewDto bookReviewDto, String userId) {
    checkIfBookExist(bookReviewDto.bookId());
    reviewDocumentRepository.save(bookReviewMapper.toReviewDocument(bookReviewDto, userId));
  }

  public List<BookReviewDto> getBookReviews(String bookId) {
    checkIfBookExist(bookId);
    return reviewDocumentRepository.findAllByBookId(bookId)
            .stream()
            .map(bookReviewMapper::toBookReviewDto)
            .toList();
  }

  private void checkIfBookExist(String bookId) {
    if (!bookRepository.existsById(bookId)) {
      throw new BookNotFoundException(bookId);
    }
  }

  public record BookReviewDto(String bookId, Integer rating, String comment, Instant timestamp, String userId) {
  }
}
