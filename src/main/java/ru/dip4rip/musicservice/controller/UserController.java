package ru.dip4rip.musicservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dip4rip.musicservice.dto.request.UserRequest;
import ru.dip4rip.musicservice.dto.response.PlaylistResponse;
import ru.dip4rip.musicservice.dto.response.UserResponse;
import ru.dip4rip.musicservice.service.UserService;

import java.util.List;

@Tag(name = "User", description = "Управление пользователями")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

  UserService userService;

  @Operation(
      description = "Получение пользователя по id",
      security = @SecurityRequirement(name = "basicAuth")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))
          }),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен")
  })
  @GetMapping("/{id}")
  public UserResponse findById(@PathVariable long id) {
    return userService.findById(id);
  }

  @Operation(
      description = "Получение всех пользователей",
      security = @SecurityRequirement(name = "basicAuth")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
          }),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен")
  })
  @GetMapping()
  public List<UserResponse> findAll() {
    return userService.findAll();
  }

  @Operation(
      description = "Создание пользователя (регистрация). Роль по умолчанию: USER",
      summary = "Регистрация нового пользователя"
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно создан",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))
          }),
      @ApiResponse(responseCode = "400", description = "Некорректные данные")
  })
  @PostMapping
  public UserResponse create(@RequestBody UserRequest userRequest) {
    return userService.create(userRequest);
  }
}
