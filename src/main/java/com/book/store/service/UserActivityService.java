package com.book.store.service;

import com.book.store.domain.UserActivityLogDocument;
import com.book.store.mapper.UserActivityLogMapper;
import com.book.store.repository.UserActivityLogDocumentRepository;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivityService {

  private final UserActivityLogDocumentRepository userActivityLogDocumentRepository;
  private final UserActivityLogMapper userActivityLogMapper;

  public void logUserActivity(UserActivityLogDto activityLogDto, String userId) {
    userActivityLogDocumentRepository.findById(userId).ifPresentOrElse(document -> {
      document.getActivityLogs().add(userActivityLogMapper.toUserActivityLog(activityLogDto));
      userActivityLogDocumentRepository.save(document);
    }, () -> userActivityLogDocumentRepository.save(userActivityLogMapper.toUserActivityLogDocument(activityLogDto, userId)));
  }

  public List<UserActivityLogDto> getUserActivityLogs(String userId) {
    return userActivityLogDocumentRepository.findById(userId)
            .map(UserActivityLogDocument::getActivityLogs)
            .stream()
            .flatMap(Collection::stream)
            .map(userActivityLogMapper::toUserActivityLogDto)
            .toList();
  }

  public record UserActivityLogDto(String activityType, String details, Instant timestamp) {
  }
}
