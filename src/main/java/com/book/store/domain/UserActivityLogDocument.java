package com.book.store.domain;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document(collection = "user_activity_logs")
public class UserActivityLogDocument {

  @MongoId
  @Field(name = "user_id")
  private String userId;

  @Field(name = "activity_logs", targetType = FieldType.IMPLICIT)
  private List<ActivityLog> activityLogs;

  @Getter
  @Setter
  public static class ActivityLog {

    private Instant timestamp = Instant.now();

    private String details;

    @Field(name = "activity_type")
    private String activityType;
  }
}
