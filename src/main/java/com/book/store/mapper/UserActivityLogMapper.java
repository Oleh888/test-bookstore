package com.book.store.mapper;

import com.book.store.domain.UserActivityLogDocument;
import com.book.store.service.UserActivityService;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserActivityLogMapper {

  UserActivityLogDocument.ActivityLog toUserActivityLog(UserActivityService.UserActivityLogDto activityLogDto);

  @Mapping(target = "activityLogs", source = "activityLogDto")
  UserActivityLogDocument toUserActivityLogDocument(UserActivityService.UserActivityLogDto activityLogDto, String userId);

  default List<UserActivityLogDocument.ActivityLog> toUserActivityLogs(UserActivityService.UserActivityLogDto activityLogDto) {
    return List.of(toUserActivityLog(activityLogDto));
  }

  UserActivityService.UserActivityLogDto toUserActivityLogDto(UserActivityLogDocument.ActivityLog activityLog);

}
