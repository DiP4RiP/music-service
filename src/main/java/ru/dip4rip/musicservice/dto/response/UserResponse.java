package ru.dip4rip.musicservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Builder
@Schema(description = "Пользователь")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  @Schema(description = "Идентификатор")
  Integer id;
  @Schema(description = "Имя")
  String fullName;
  @Schema(description = "Адрес")
  String address;
  @Schema(description = "Номер телефона")
  String phone;
  @Schema(description = "Логин")
  String login;
}
