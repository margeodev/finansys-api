package com.finansys.finansys_api.domain.service;

import com.finansys.finansys_api.domain.model.User;
import com.finansys.finansys_api.exception.UserNotFoundException;
import com.finansys.finansys_api.repository.UserRepository;
import com.finansys.finansys_api.web.mapper.UserMapper;
import com.finansys.finansys_api.web.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findByPhoneNumber(String phoneNumber) {
        User user =  userRepository.findByPhoneNumberAndIsActiveTrue(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("telefone", phoneNumber));

        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> findAll() {
        List<User> user = userRepository.findAll();
        return userMapper.toUserResponseList(user);
    }

    public UserResponse findByEmail(String email) {
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));

        return userMapper.toUserResponse(user);
    }

}

