package ru.dip4rip.musicservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.dip4rip.musicservice.converter.PlaylistConverter;
import ru.dip4rip.musicservice.dto.request.PlaylistMusicRequest;
import ru.dip4rip.musicservice.dto.request.PlaylistRequest;
import ru.dip4rip.musicservice.dto.response.PlaylistResponse;
import ru.dip4rip.musicservice.models.Playlist;
import ru.dip4rip.musicservice.models.PlaylistMusic;
import ru.dip4rip.musicservice.models.PlaylistMusicId;
import ru.dip4rip.musicservice.repository.MusicRepository;
import ru.dip4rip.musicservice.repository.PlaylistMusicRepository;
import ru.dip4rip.musicservice.repository.PlaylistRepository;
import ru.dip4rip.musicservice.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaylistService {

  PlaylistRepository playlistRepository;
  PlaylistConverter playlistConverter;
  UserRepository userRepository;
  MusicRepository musicRepository;
  PlaylistMusicRepository playlistMusicRepository;


  public PlaylistResponse findById(long id) {
    return playlistRepository.findById(id)
        .map(playlistConverter::toDto)
        .orElseThrow(() -> new RuntimeException(String.format("Плейлист с номером: %s не найден", id)));
  }

  public List<PlaylistResponse> findAll() {
    return playlistRepository.findAll().stream()
        .map(playlistConverter::toDto)
        .toList();
  }

  public PlaylistResponse create(PlaylistRequest request) {
    return userRepository.findById(request.getUserId())
        .map((user) -> playlistConverter.toEntity(request, user))
        .map(playlistRepository::save)
        .map(playlistConverter::toDto)
        .orElseThrow(() -> new RuntimeException(String.format("Пользователь с номером: %s не найден", request.getUserId())));
  }

  public PlaylistResponse addMusic(PlaylistMusicRequest request) {
    Long playlistId = request.getPlaylistId();
    Playlist playlist = playlistRepository.findById(playlistId)
        .orElseThrow(() -> new RuntimeException(String.format("Плейлист с номером: %s не найдена", playlistId)));
    List<PlaylistMusic> playlistMusics = Optional.ofNullable(request.getInventoryNumbers())
        .map(numbers -> numbers.stream()
            .map(inventoryNumber -> musicRepository.findById(inventoryNumber)
                .orElseThrow(() -> new RuntimeException(String.format("Музыка с номером: %s не найдена", inventoryNumber))))
            .map(music -> {
              PlaylistMusic playlistMusic = new PlaylistMusic();
              playlistMusic.setId(new PlaylistMusicId());
              playlistMusic.setMusic(music);
              playlistMusic.setPlaylist(playlist);
              return playlistMusic;
            }).toList())
        .orElse(new ArrayList<>());
    playlistMusicRepository.saveAll(playlistMusics);
    return playlistConverter.toDto(playlist);
  }
}
