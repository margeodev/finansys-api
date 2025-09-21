package com.finansys.finansys_api.web.mapper;

import com.finansys.finansys_api.domain.model.User;
import com.finansys.finansys_api.web.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper mapper;

    public User toUser(UserResponse response) {
        return mapper.map(response, User.class);
    }

    public UserResponse toUserResponse(User user) {
        return mapper.map(user, UserResponse.class);
    }

    public List<UserResponse> toUserResponseList(List<User> users) {
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }
}
