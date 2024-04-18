package ru.dip4rip.musicservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.dip4rip.musicservice.converter.MusicConverter;
import ru.dip4rip.musicservice.dto.response.MusicResponse;
import ru.dip4rip.musicservice.repository.MusicRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicService {

  MusicRepository musicRepository;
  MusicConverter musicConverter;

  public MusicResponse findById(long id) {
    return musicRepository.findById(id)
        .map(musicConverter::toDto)
        .orElseThrow(() -> new RuntimeException(String.format("Музыка с номером: %s не найдена", id)));
  }

  public List<MusicResponse> findAll() {
    return musicRepository.findAll().stream().map(musicConverter::toDto).toList();
  }
}
