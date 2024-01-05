package com.book.store.domain;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document(collection = "reviews")
public class ReviewDocument {

  @MongoId
  private ObjectId id;

  @Field(name = "book_id")
  @Indexed(name = "book_id_index")
  private String bookId;

  @Field(name = "user_id")
  @Indexed(name = "user_id_index")
  private String userId;

  private Integer rating;

  private String comment;

  @CreatedDate
  private Instant timestamp;
}
