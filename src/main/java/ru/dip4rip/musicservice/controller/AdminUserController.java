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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dip4rip.musicservice.dto.response.UserResponse;
import ru.dip4rip.musicservice.service.UserService;

import java.util.List;

@Tag(name = "Admin User Management", description = "Административное управление пользователями. Требует роль ADMIN")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserController {

  UserService userService;

  @Operation(
      description = "Получение всех пользователей. Требует роль ADMIN",
      summary = "Список всех пользователей",
      security = @SecurityRequirement(name = "basicAuth")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
          }),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Требуется роль ADMIN")
  })
  @GetMapping
  public List<UserResponse> getAllUsers() {
    return userService.findAll();
  }

  @Operation(
      description = "Получение пользователя по ID. Требует роль ADMIN",
      summary = "Информация о пользователе",
      security = @SecurityRequirement(name = "basicAuth")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = UserResponse.class))
          }),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Требуется роль ADMIN"),
      @ApiResponse(responseCode = "404", description = "Пользователь не найден")
  })
  @GetMapping("/{id}")
  public UserResponse getUserById(@PathVariable long id) {
    return userService.findById(id);
  }

  @Operation(
      description = "Удаление пользователя. Требует роль ADMIN",
      summary = "Удалить пользователя",
      security = @SecurityRequirement(name = "basicAuth")
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
      @ApiResponse(responseCode = "401", description = "Не авторизован"),
      @ApiResponse(responseCode = "403", description = "Требуется роль ADMIN"),
      @ApiResponse(responseCode = "404", description = "Пользователь не найден")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable long id) {
    userService.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
