package ru.dip4rip.musicservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Builder
@Schema(description = "Создание плейлиста")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaylistRequest {
  @Schema(description = "Наименование плейлиста")
  String name;
  @Schema(description = "Идентификатор пользователя")
  Long userId;
}
