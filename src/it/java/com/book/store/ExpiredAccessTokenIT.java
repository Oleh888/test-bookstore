package com.book.store;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = "auth.token.expiration:0")
class ExpiredAccessTokenIT extends AbstractIT {

  @Test
  void shouldRespondUnauthorizedWhenTokenIsExpired() {
    buildRestAssuredWithTestToken().when()
            .post("/api/books")
            .then().assertThat().log().all()
            .statusCode(UNAUTHORIZED.value());
  }
}
