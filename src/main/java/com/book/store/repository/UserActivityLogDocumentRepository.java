package com.book.store.repository;

import com.book.store.domain.UserActivityLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserActivityLogDocumentRepository extends MongoRepository<UserActivityLogDocument, String> {
}
