package com.finansys.finansysapi.domain.service;

import com.finansys.finansysapi.api.mapper.UserMapper;
import com.finansys.finansysapi.api.request.user.LoginRequest;
import com.finansys.finansysapi.api.response.UserResponse;
import com.finansys.finansysapi.domain.model.User;
import com.finansys.finansysapi.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse createUser(LoginRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔑 senha criptografada
        user.setIsActive(true);
        user.setCreatedAt(LocalDate.now());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        User userCreated =  userRepository.save(user);
        return userMapper.toUserResponse(userCreated);
    }

    public UserResponse findByPhoneNumber(String phoneNumber) {
        User user =  userRepository.findByPhoneNumberAndIsActiveTrue(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> findAll() {
        List<User> user = userRepository.findAll();
        return userMapper.toUserResponseList(user);
    }

    public UserResponse findByEmail(String email) {
        User user =  userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return userMapper.toUserResponse(user);
    }
}

