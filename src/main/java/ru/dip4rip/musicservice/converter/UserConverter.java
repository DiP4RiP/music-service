package ru.dip4rip.musicservice.converter;

import org.springframework.stereotype.Component;
import ru.dip4rip.musicservice.dto.request.UserRequest;
import ru.dip4rip.musicservice.dto.response.UserResponse;
import ru.dip4rip.musicservice.models.User;

@Component
public class UserConverter {

  public UserResponse toDto(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .fullName(user.getFullName())
        .address(user.getAddress())
        .phone(user.getPhone())
        .login(user.getLogin())
        .build();
  }

  public User toEntity(UserRequest userRequest) {
    User user = new User();
    user.setAddress(userRequest.getAddress());
    user.setPhone(userRequest.getPhone());
    user.setLogin(userRequest.getLogin());
    user.setPassword(userRequest.getPassword());
    user.setFullName(userRequest.getFullName());
    return user;
  }
}
