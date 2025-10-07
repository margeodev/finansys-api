package com.finansys.finansys_api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {
  private final JwtEncoder encoder;

  @Value("${app.jwt.access-expiration:3600}") // 1 hora em segundos
  private long accessTokenExpiration;

  public JwtService(JwtEncoder encoder) {
    this.encoder = encoder;
  }

  public String generateToken(Authentication authentication) {
    return generateAccessToken(authentication);
  }

  public String generateAccessToken(Authentication authentication) {
    Instant now = Instant.now();
    String scope = authentication
            .getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors
                    .joining(" "));
    JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("spring-security-jwt")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(accessTokenExpiration))
            .subject(authentication.getName())
            .claim("scope", scope)
            .claim("type", "access")
            .build();
    return encoder.encode(
                    JwtEncoderParameters.from(claims))
            .getTokenValue();
  }

  public long getAccessTokenExpiration() {
    return accessTokenExpiration;
  }

}