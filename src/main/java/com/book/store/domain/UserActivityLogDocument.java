package com.book.store.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@Document(collation = "user_activity_log")
public class UserActivityLogDocument {

  @MongoId
  private ObjectId id;

  @Field(name = "user_id")
  @Indexed(name = "user_id_index")
  private String userId;

  @Field(name = "activity_logs", targetType = FieldType.IMPLICIT)
  private List<ActivityLog> activityLogs;


  public record ActivityLog(Long timestamp, String details, @Field(name = "activity_type") String activityType) {
  }
}
