package com.finansys.finansysapi.domain.service;

import com.finansys.finansysapi.api.request.user.LoginRequest;
import com.finansys.finansysapi.api.request.user.LoginResponse;
import com.finansys.finansysapi.domain.model.User;
import com.finansys.finansysapi.domain.repository.UserRepository;
import com.finansys.finansysapi.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        User user = new User();
        if(request.getEmail() != null) {
            user = userRepository.findByEmailAndIsActiveTrue(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        } else if (request.getPhoneNumber() != null) {
            user = userRepository.findByPhoneNumberAndIsActiveTrue(request.getPhoneNumber())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(token);
    }
}

