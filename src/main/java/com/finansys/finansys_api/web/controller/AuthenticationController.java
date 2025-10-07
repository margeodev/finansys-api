package com.finansys.finansys_api.web.controller;
import com.finansys.finansys_api.domain.dto.request.RefreshTokenRequest;
import com.finansys.finansys_api.domain.dto.response.AuthenticationResponse;
import com.finansys.finansys_api.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("auth/login")
  public ResponseEntity<AuthenticationResponse> authenticate(Authentication authentication) {
    AuthenticationResponse response = authenticationService.authenticate(authentication);
    return ResponseEntity.ok(response);
  }

  @PostMapping("auth/refresh")
  public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
    AuthenticationResponse response = authenticationService.refreshToken(request.getRefreshToken());
    return ResponseEntity.ok(response);
  }
}