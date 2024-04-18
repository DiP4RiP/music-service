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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dip4rip.musicservice.dto.response.MusicResponse;
import ru.dip4rip.musicservice.service.MusicService;

import java.util.List;

@Tag(name = "Music", description = "Управление музыкой")
@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicController {

  MusicService musicService;

  @Operation(description = "Получение песни по идентификатору")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = MusicResponse.class))
          })
  })
  @GetMapping("/{id}")
  public MusicResponse findById(@PathVariable long id) {
    return musicService.findById(id);
  }

  @Operation(description = "Получение всех песен")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = MusicResponse.class)))
          })
  })
  @GetMapping
  public List<MusicResponse> findAll() {
    return musicService.findAll();
  }
}
