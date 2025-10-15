package ru.dip4rip.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dip4rip.musicservice.dto.request.MusicRequest;
import ru.dip4rip.musicservice.dto.response.MusicResponse;
import ru.dip4rip.musicservice.service.MusicService;

@Tag(name = "Admin Music Management", description = "Административное управление музыкой. Требует роль ADMIN")
@RestController
@RequestMapping("/api/admin/music")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMusicController {

  MusicService musicService;

  @Operation(
      description = "Добавление новой песни. Требует роль ADMIN",
      summary = "Добавить новую песню в каталог",
      security = @SecurityRequirement(name = "basicAuth")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Песня успешно добавлена",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = MusicResponse.class))
          }),
      @ApiResponse(responseCode = "400", description = "Некорректные данные"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Требуется роль ADMIN")
  })
  @PostMapping
  public MusicResponse createMusic(@RequestBody MusicRequest musicRequest) {
    return musicService.create(musicRequest);
  }

  @Operation(
      description = "Удаление песни. Требует роль ADMIN",
      summary = "Удалить песню из каталога",
      security = @SecurityRequirement(name = "basicAuth")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Песня успешно удалена"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Требуется роль ADMIN"),
      @ApiResponse(responseCode = "404", description = "Песня не найдена")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteMusic(@PathVariable long id) {
    musicService.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
