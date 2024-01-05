package com.book.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.domain.UserActivityLogDocument;
import org.junit.jupiter.api.Test;

class UserActivityLogIT extends AbstractIT {

  @Test
  void logUserActivityShouldRespondCreated() {
    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "activity-type": "visit",
                      "details": "visited home page"
                    }""")
            .post("/api/users/activity-log")
            .then().assertThat().log().all()
            .statusCode(CREATED.value());

    assertThat(userActivityLogRepository.findAll()).hasSize(1).element(0)
            .hasFieldOrPropertyWithValue("userId", "user_id")
            .extracting(UserActivityLogDocument::getActivityLogs)
            .asList()
            .hasSize(1)
            .element(0)
            .extracting("activityType", "details")
            .containsExactly("visit", "visited home page");
  }

  @Test
  void logUserActivityShouldAppendLogAndRespondCreated() {
    saveUserActivityLog("user_id", "User left a comment", "comment");

    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "activity-type": "visit",
                      "details": "visited home page"
                    }""")
            .post("/api/users/activity-log")
            .then().assertThat().log().all()
            .statusCode(CREATED.value());

    assertThat(userActivityLogRepository.findAll()).hasSize(1).element(0)
            .hasFieldOrPropertyWithValue("userId", "user_id")
            .extracting(UserActivityLogDocument::getActivityLogs)
            .asList()
            .hasSize(2)
            .flatExtracting("activityType", "details")
            .containsExactly("comment", "User left a comment", "visit", "visited home page");
  }

  @Test
  void getUserActivityLogsShouldRespondOk() {
    saveUserActivityLog("user_id", "User left a comment", "comment");

    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .get("/api/users/activity-log")
            .then().assertThat().log().all()
            .statusCode(OK.value())
            .body("size()", is(1))
            .body("[0].activity-type", equalTo("comment"))
            .body("[0].details", equalTo("User left a comment"))
            .body("[0].timestamp", notNullValue());
  }
}
