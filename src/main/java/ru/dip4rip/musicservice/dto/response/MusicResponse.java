package ru.dip4rip.musicservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Value
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Музыка")
public class MusicResponse {
  @Schema(description = "Инвентарный номер")
  String inventoryNumber;
  @Schema(description = "Жанр")
  String genre;
  @Schema(description = "Наименование")
  String title;
  @Schema(description = "Исполнитель")
  String performer;
  @Schema(description = "Композитор")
  String composerName;
  @Schema(description = "Тип устройства")
  String mediaTypeName;
  @Schema(description = "Студия записи")
  String recordLabelName;
  @Schema(description = "Дата записи")
  LocalDate recordingDate;
}
