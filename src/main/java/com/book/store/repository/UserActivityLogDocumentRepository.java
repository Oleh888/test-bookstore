package com.book.store.repository;

import com.book.store.domain.UserActivityLogDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityLogDocumentRepository extends MongoRepository<UserActivityLogDocument, String> {
}
