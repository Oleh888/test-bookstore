package com.book.store.auth;

import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("user_id")
public class UserIdAccessor implements Supplier<String> {

  private final ThreadLocal<String> userIdValue = new ThreadLocal<>();

  @Override
  public String get() {
    return userIdValue.get();
  }

  public void store(String userId) {
    userIdValue.set(userId);
  }

  public void clean() {
    userIdValue.remove();
  }
}
