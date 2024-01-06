package com.book.store.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtHandler {

  @Value("${auth.key}")
  private String authKey;

  @Value("${auth.token.expiration}")
  private short tokenExpirationHours;

  public String generateToken(String userId) {
    return Jwts.builder()
            .setSubject(userId)
            .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(tokenExpirationHours))))
            .signWith(SignatureAlgorithm.HS512, authKey)
            .compact();
  }

  public String extractUserId(String token) {
    return Jwts.parser()
            .setSigningKey(authKey)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }
}
