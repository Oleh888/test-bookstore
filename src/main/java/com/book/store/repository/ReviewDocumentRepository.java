package com.book.store.repository;

import com.book.store.domain.ReviewDocument;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewDocumentRepository extends MongoRepository<ReviewDocument, String> {

  List<ReviewDocument> findAllByBookId(String bookId);
}
