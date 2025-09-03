package com.finansys.finansysapi.api.controller;

import com.finansys.finansysapi.api.response.UserResponse;
import com.finansys.finansysapi.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    @GetMapping("/phone-number")
    public ResponseEntity<UserResponse> findUserByPhoneNumber(@RequestHeader("phoneNumber") String phoneNumber) {
        UserResponse response =  service.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public ResponseEntity<UserResponse> findUserByEmail(@RequestHeader("email") String email) {
        UserResponse response =  service.findByEmail(email);
        return ResponseEntity.ok(response);
    }
}
