package com.book.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.book.store.auth.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthIT extends AbstractIT {

  @Autowired
  private UserService userService;

  @Test
  void shouldSaveUserAndRespondCreated() {
    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "password": "strong password",
                      "username": "Uncle Bob"
                    }""")
            .post("/api/users/create")
            .then().assertThat().log().all()
            .statusCode(CREATED.value());

    assertThat(userRepository.findByUsername("Uncle Bob")).isNotEmpty();
  }

  @Test
  void shouldNotSaveUserAndRespondBadRequestForNotUniqueUsername() {
    saveUser("Uncle Bob", "password");

    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "password": "strong password",
                      "username": "Uncle Bob"
                    }""")
            .post("/api/users/create")
            .then().assertThat().log().all()
            .statusCode(BAD_REQUEST.value())
            .body("error", equalTo("not-unique-username"))
            .body("details", equalTo("Couldn't create user with not unique username"));;

    assertThat(userRepository.findAll()).hasSize(1);
  }

  @Test
  void shouldLoginAndRespondWithAccessToken() {
    userService.saveUser("Uncle Bob", "strong password");

    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "password": "strong password",
                      "username": "Uncle Bob"
                    }""")
            .post("/api/login")
            .then().assertThat().log().all()
            .statusCode(OK.value())
            .body("token", notNullValue());
  }

  @Test
  void shouldNotLoginAndRespondWithUnauthorizedForWrongUsername() {
    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "password": "strong password",
                      "username": "Uncle Bob"
                    }""")
            .post("/api/login")
            .then().assertThat().log().all()
            .statusCode(UNAUTHORIZED.value())
            .body("error", equalTo("not-authenticate"))
            .body("details", equalTo("Username or password wasn't correct"));;
  }

  @Test
  void shouldNotLoginAndRespondWithUnauthorizedForWrongPassword() {
    userService.saveUser("Uncle Bob", "strong password");

    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .body("""
                    {
                      "password": "wrong password",
                      "username": "Uncle Bob"
                    }""")
            .post("/api/login")
            .then().assertThat().log().all()
            .statusCode(UNAUTHORIZED.value())
            .body("error", equalTo("not-authenticate"))
            .body("details", equalTo("Username or password wasn't correct"));;
  }

  @Test
  void shouldRespondUnauthorizedWhenTokenIsMissed() {
    buildRestAssured().when()
            .contentType(APPLICATION_JSON_VALUE)
            .get("/api/users/activity-log")
            .then().assertThat().log().all()
            .statusCode(UNAUTHORIZED.value());
  }
}
