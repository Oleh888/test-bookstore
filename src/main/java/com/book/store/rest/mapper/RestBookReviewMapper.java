package com.book.store.rest.mapper;

import com.book.store.rest.BookReviewController;
import com.book.store.service.BookReviewService;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TimestampMapper.class)
public interface RestBookReviewMapper {

  BookReviewService.BookReviewDto toBookReviewDto(BookReviewController.SaveBookReviewRequest request, String bookId);

  BookReviewController.BookReviewResponse toBookReviewResponse(BookReviewService.BookReviewDto reviewDto);

  List<BookReviewController.BookReviewResponse> toBookReviewResponses(List<BookReviewService.BookReviewDto> reviewsDto);

}
