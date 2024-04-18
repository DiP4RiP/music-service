package ru.dip4rip.musicservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.dip4rip.musicservice.converter.UserConverter;
import ru.dip4rip.musicservice.dto.request.UserRequest;
import ru.dip4rip.musicservice.dto.response.UserResponse;
import ru.dip4rip.musicservice.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

  UserRepository userRepository;
  UserConverter userConverter;

  public UserResponse findById(long id) {
    return userRepository.findById(id)
        .map(userConverter::toDto)
        .orElseThrow(() -> new RuntimeException(String.format("Пользователь с номером: %s не найден", id)));
  }

  public List<UserResponse> findAll() {
    return userRepository.findAll().stream().map(userConverter::toDto).toList();
  }

  public UserResponse create(UserRequest userRequest) {
    return Optional.ofNullable(userRequest)
        .map(userConverter::toEntity)
        .map(userRepository::save)
        .map(userConverter::toDto)
        .orElseThrow(() -> new RuntimeException("Произошла ошибка при сохранении"));
  }
}
