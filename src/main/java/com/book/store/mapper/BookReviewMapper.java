package com.book.store.mapper;

import com.book.store.domain.ReviewDocument;
import com.book.store.service.BookReviewService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookReviewMapper {

  @Mapping(target = "userId", source = "userId")
  ReviewDocument toReviewDocument(BookReviewService.BookReviewDto reviewDto, String userId);

  BookReviewService.BookReviewDto toBookReviewDto(ReviewDocument reviewDocument);

}
