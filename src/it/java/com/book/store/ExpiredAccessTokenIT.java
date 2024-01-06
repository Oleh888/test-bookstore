package com.book.store;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "auth.token.expiration:0")
class ExpiredAccessTokenIT extends AbstractIT {

  @Test
  void shouldRespondUnauthorizedWhenTokenIsMissed() {
    buildRestAssuredWithTestToken().when()
            .contentType(APPLICATION_JSON_VALUE)
            .get("/api/users/activity-log")
            .then().assertThat().log().all()
            .statusCode(UNAUTHORIZED.value());
  }
}
