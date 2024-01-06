package com.book.store.auth;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

  private static final String ACCESS_TOKEN_HEADER = "X-ACCESS-TOKEN";
  private static final Set<String> ALLOWED_PATH = Set.of("/api/login", "/api/users/create", "/graphql", "/api/books");

  private final UserIdAccessor userIdAccessor;
  private final JwtHandler jwtHandler;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
    var httpServletRequest = ((HttpServletRequest) request);
    if (isPathAllowed(httpServletRequest.getServletPath())) {
      chain.doFilter(request, response);
    } else {
      Optional.ofNullable(httpServletRequest.getHeader(ACCESS_TOKEN_HEADER))
              .filter(StringUtils::hasText)
              .flatMap(this::extractUserIdFromAccessToken)
              .ifPresent(userIdAccessor::store);

      var httpServletResponse = (HttpServletResponse) response;
      if (StringUtils.hasText(userIdAccessor.get())) {
        chain.doFilter(httpServletRequest, httpServletResponse);
      } else {
        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value());
      }
    }
  }

  private Optional<String> extractUserIdFromAccessToken(String token) {
    try {
      return Optional.of(jwtHandler.extractUserId(token));
    } catch (Exception e) {
      log.warn("Couldn't extract details from access token", e);
      return Optional.empty();
    }
  }

  private boolean isPathAllowed(String path) {
    return ALLOWED_PATH.stream().anyMatch(path::startsWith);
  }
}
