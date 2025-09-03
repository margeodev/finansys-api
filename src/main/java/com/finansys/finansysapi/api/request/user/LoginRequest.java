package com.finansys.finansysapi.api.request.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
}

