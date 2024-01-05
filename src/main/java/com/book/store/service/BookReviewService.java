package com.book.store.service;

import com.book.store.mapper.BookReviewMapper;
import com.book.store.repository.ReviewDocumentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewService {

  private final ReviewDocumentRepository reviewDocumentRepository;
  private final BookReviewMapper bookReviewMapper;

  public void saveReview(BookReviewDto bookReviewDto, String userId) {
    reviewDocumentRepository.save(bookReviewMapper.toReviewDocument(bookReviewDto, userId));
  }

  public List<BookReviewDto> getBookReviews(String bookId) {
    return reviewDocumentRepository.findAllByBookId(bookId)
            .stream()
            .map(bookReviewMapper::toBookReviewDto)
            .toList();
  }

  public record BookReviewDto(String bookId, Integer rating, String comment, Long timestamp, String userId) {
  }
}
