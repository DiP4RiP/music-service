package ru.dip4rip.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dip4rip.musicservice.dto.request.PlaylistMusicRequest;
import ru.dip4rip.musicservice.dto.request.PlaylistRequest;
import ru.dip4rip.musicservice.dto.response.MusicResponse;
import ru.dip4rip.musicservice.dto.response.PlaylistResponse;
import ru.dip4rip.musicservice.service.PlaylistService;

import java.util.List;

@Tag(name = "Playlist", description = "Управление плейлистами")
@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaylistController {

  PlaylistService playlistService;

  @Operation(description = "Получение плейлиста по идентификатору")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PlaylistResponse.class))
          })
  })
  @GetMapping("/{id}")
  public PlaylistResponse findById(@PathVariable long id) {
    return playlistService.findById(id);
  }

  @Operation(description = "Получение всех плейлистов")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = PlaylistResponse.class)))
          })
  })
  @GetMapping()
  public List<PlaylistResponse> findAll() {
    return playlistService.findAll();
  }

  @Operation(description = "Создание плейлиста")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PlaylistResponse.class))
          })
  })
  @PostMapping
  public PlaylistResponse create(@RequestBody PlaylistRequest request) {
    return playlistService.create(request);
  }

  @Operation(description = "Добавление музыки")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = PlaylistResponse.class))
          })
  })
  @PostMapping("/music")
  public PlaylistResponse addMusic(@RequestBody PlaylistMusicRequest request) {
    return playlistService.addMusic(request);
  }
}
