package com.book.store.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.store.domain.UserActivityLogDocument;
import com.book.store.mapper.UserActivityLogMapper;
import com.book.store.repository.UserActivityLogDocumentRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

class UserActivityServiceTest extends AbstractTest {

  @Mock
  private UserActivityLogDocumentRepository userActivityLogDocumentRepository;

  @Spy
  private UserActivityLogMapper userActivityLogMapper = Mappers.getMapper(UserActivityLogMapper.class);

  @InjectMocks
  private UserActivityService userActivityService;

  @Test
  void shouldLogUserActivity() {
    when(userActivityLogDocumentRepository.findById("user_id")).thenReturn(Optional.empty());
    when(userActivityLogDocumentRepository.save(any(UserActivityLogDocument.class))).thenReturn(getUserActivityLogDocument());

    userActivityService.logUserActivity(getUserActivityLogDto(), "user_id");

    verify(userActivityLogDocumentRepository, times(1)).findById("user_id");
    verify(userActivityLogDocumentRepository, times(1)).save(any(UserActivityLogDocument.class));
  }

  @Test
  void shouldAppendLogUserActivity() {
    when(userActivityLogDocumentRepository.findById("user_id")).thenReturn(Optional.of(getUserActivityLogDocument()));
    when(userActivityLogDocumentRepository.save(any(UserActivityLogDocument.class))).thenReturn(getUserActivityLogDocument());

    userActivityService.logUserActivity(getUserActivityLogDto(), "user_id");

    verify(userActivityLogDocumentRepository, times(1)).findById("user_id");
    verify(userActivityLogDocumentRepository, times(1)).save(any(UserActivityLogDocument.class));
  }

  @Test
  void shouldReturnUserActivityLogs() {
    when(userActivityLogDocumentRepository.findById("user_id")).thenReturn(Optional.of(getUserActivityLogDocument()));

    var activityLogs = userActivityService.getUserActivityLogs("user_id");

    assertThat(activityLogs).hasSize(1).flatExtracting(UserActivityService.UserActivityLogDto::activityType, UserActivityService.UserActivityLogDto::details)
            .containsExactly("visit", "User visited home page");
  }

  @Test
  void shouldReturnUserActivityLogsEmpty() {
    when(userActivityLogDocumentRepository.findById("user_id")).thenReturn(Optional.empty());

    var activityLogs = userActivityService.getUserActivityLogs("user_id");

    assertThat(activityLogs).isEmpty();
  }
}
