package com.book.store.auth;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

  private final UserIdAccessor userIdAccessor;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    userIdAccessor.store("user_id");
    chain.doFilter(request, response);
  }
}
