package ru.dip4rip.musicservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Value
@Builder
@Schema(name = "PlaylistMusic", description = "Добавление музыки в плейлист")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlaylistMusicRequest {
  @Schema(description = "Идентификатор плейлиста")
  Long playlistId;
  @Schema(description = "Список идентификаторов музыки")
  List<Long> inventoryNumbers;
}
