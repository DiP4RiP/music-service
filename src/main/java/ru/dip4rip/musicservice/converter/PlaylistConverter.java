package ru.dip4rip.musicservice.converter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.dip4rip.musicservice.dto.request.PlaylistRequest;
import ru.dip4rip.musicservice.dto.response.PlaylistResponse;
import ru.dip4rip.musicservice.models.Playlist;
import ru.dip4rip.musicservice.models.PlaylistMusic;
import ru.dip4rip.musicservice.models.User;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaylistConverter {

  UserConverter userConverter;
  MusicConverter musicConverter;

  public PlaylistResponse toDto(Playlist playlist) {
    return PlaylistResponse.builder()
        .id(playlist.getId())
        .name(playlist.getName())
        .user(userConverter.toDto(playlist.getUser()))
        .musicList(Optional.ofNullable(playlist.getPlaylistMusics())
            .map(playlistMusics -> playlistMusics.stream()
                .map(PlaylistMusic::getMusic)
                .map(musicConverter::toDto)
                .toList())
            .orElse(new ArrayList<>()))
        .build();
  }

  public Playlist toEntity(PlaylistRequest request, User user) {
    Playlist playlist = new Playlist();
    playlist.setName(request.getName());
    playlist.setUser(user);
    return playlist;
  }
}
