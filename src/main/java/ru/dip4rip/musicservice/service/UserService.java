package ru.dip4rip.musicservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  PasswordEncoder passwordEncoder;

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
        .map(user -> {
          // Хешируем пароль перед сохранением
          user.setPassword(passwordEncoder.encode(user.getPassword()));
          return user;
        })
        .map(userRepository::save)
        .map(userConverter::toDto)
        .orElseThrow(() -> new RuntimeException("Произошла ошибка при сохранении"));
  }

  @Transactional
  public void deleteById(long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException(String.format("Пользователь с номером: %s не найден", id));
    }
    
    // JPA автоматически удалит связанные плейлисты и playlist_music записи
    // благодаря каскадному удалению в моделях
    userRepository.deleteById(id);
  }
}
