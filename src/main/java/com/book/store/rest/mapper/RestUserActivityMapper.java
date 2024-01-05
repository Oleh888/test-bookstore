package com.book.store.rest.mapper;

import com.book.store.rest.UserActivityLogController;
import com.book.store.service.UserActivityService;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TimestampMapper.class)
public interface RestUserActivityMapper {

  UserActivityService.UserActivityLogDto toUserActivityLogDto(UserActivityLogController.LogUserActivityRequest logUserActivityRequest);

  List<UserActivityLogController.UserActivityResponse> toUserActivityResponse(List<UserActivityService.UserActivityLogDto> activityLogsDto);

  UserActivityLogController.UserActivityResponse toUserActivityResponse(UserActivityService.UserActivityLogDto activityLogDto);
}
