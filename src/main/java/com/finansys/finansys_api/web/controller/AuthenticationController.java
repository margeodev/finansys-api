package com.finansys.finansys_api.web.controller;

import com.finansys.finansys_api.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("authenticate")
  public String authenticate(
      Authentication authentication) {
    return authenticationService.authenticate(authentication);
  }
}
