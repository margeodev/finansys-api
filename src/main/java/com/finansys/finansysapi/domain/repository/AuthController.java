package com.finansys.finansysapi.domain.repository;

import com.finansys.finansysapi.api.mapper.UserMapper;
import com.finansys.finansysapi.api.request.user.LoginRequest;
import com.finansys.finansysapi.api.request.user.LoginResponse;
import com.finansys.finansysapi.api.response.UserResponse;
import com.finansys.finansysapi.domain.service.AuthService;
import com.finansys.finansysapi.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final UserMapper mapper;

    @PostMapping("/login")
    public LoginResponse login(@RequestHeader String email, @RequestHeader String password) {
        var request = LoginRequest.builder().email(email).password(password).build();
        return authService.login(request);
    }

    @PostMapping("/login/robo")
    public LoginResponse loginRobo(@RequestHeader String phoneNumber, @RequestHeader String password) {
        var request = LoginRequest.builder()
                .phoneNumber(phoneNumber)
                .password(password)
                .build();
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody LoginRequest request) {
        UserResponse user = userService.createUser(request);

        return ResponseEntity.ok(user);
    }
}

