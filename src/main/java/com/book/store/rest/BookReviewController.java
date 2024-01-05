package com.book.store.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.rest.mapper.RestBookReviewMapper;
import com.book.store.service.BookReviewService;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/books/{book_id}/reviews")
public class BookReviewController {

  @Qualifier("user_id")
  private final Supplier<String> userIdSupplier;
  private final RestBookReviewMapper bookReviewMapper;
  private final BookReviewService bookReviewService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void saveReview(@PathVariable("book_id") String bookId, @RequestBody SaveBookReviewRequest request) {
    log.debug("Received save review request: {}", request);

    bookReviewService.saveReview(bookReviewMapper.toBookReviewDto(request, bookId), userIdSupplier.get());
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public List<BookReviewResponse> getReviews(@PathVariable("book_id") String bookId) {
    return bookReviewMapper.toBookReviewResponses(bookReviewService.getBookReviews(bookId));
  }

  public record SaveBookReviewRequest(Integer rating, String comment) {
  }

  public record BookReviewResponse(String bookId, Integer rating, String comment, Long timestamp, String userId) {
  }
}
