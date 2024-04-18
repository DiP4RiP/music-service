package ru.dip4rip.musicservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Value
@Builder
@Schema(description = "Плейлист")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaylistResponse {
  @Schema(description = "Идентификатор")
  Integer id;
  @Schema(description = "Наименование")
  String name;
  @Schema(description = "Пользователь")
  UserResponse user;
  @Schema(description = "Список музыки")
  List<MusicResponse> musicList;
}
