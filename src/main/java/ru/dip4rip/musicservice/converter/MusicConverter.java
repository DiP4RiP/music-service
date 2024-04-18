package ru.dip4rip.musicservice.converter;

import org.springframework.stereotype.Component;
import ru.dip4rip.musicservice.dto.response.MusicResponse;
import ru.dip4rip.musicservice.models.Music;

@Component
public class MusicConverter {

  public MusicResponse toDto(Music music) {
    return MusicResponse.builder()
        .performer(music.getPerformer().getName())
        .composerName(music.getComposer().getName())
        .title(music.getTitle())
        .genre(music.getGenre().getName())
        .inventoryNumber(music.getId().toString())
        .mediaTypeName(music.getMediaType().getName())
        .recordLabelName(music.getRecordLabel().getName())
        .recordingDate(music.getRecordingDate())
        .build();
  }
}
