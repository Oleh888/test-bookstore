package com.book.store.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.rest.mapper.RestUserActivityMapper;
import com.book.store.service.UserActivityService;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/users/activity-log")
public class UserActivityLogController {

  @Qualifier("user_id")
  private final Supplier<String> userIdSupplier;
  private final RestUserActivityMapper userActivityMapper;
  private final UserActivityService userActivityService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void logUserActivity(@RequestBody LogUserActivityRequest request) {
    log.debug("Received log user activity request: {}", request);

    userActivityService.logUserActivity(userActivityMapper.toUserActivityLogDto(request), userIdSupplier.get());
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public List<UserActivityResponse> getUserActivity() {
    return userActivityMapper.toUserActivityResponse(userActivityService.getUserActivityLogs(userIdSupplier.get()));
  }

  public record LogUserActivityRequest(@JsonProperty("activity-type") String activityType, String details) {
  }

  public record UserActivityResponse(@JsonProperty("activity-type") String activityType, String details,
                                     Long timestamp) {
  }
}
