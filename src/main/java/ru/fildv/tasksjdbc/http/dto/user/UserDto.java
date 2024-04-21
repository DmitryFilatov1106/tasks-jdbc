package ru.fildv.tasksjdbc.http.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import ru.fildv.tasksjdbc.http.validation.group.CreateAction;
import ru.fildv.tasksjdbc.http.validation.group.UpdateAction;

@Schema(description = "User DTO")
public record UserDto(
        @Schema(description = "User id", example = "1")
        @NotNull(message = "ID must not be null", groups = UpdateAction.class)
        Long id,
        @Schema(description = "User name", example = "Ivan Ivanov")
        @NotNull(message = "Name must not be null",
                groups = {CreateAction.class, UpdateAction.class})
        @Length(max = 255,
                message = "Name length must be less than 255 symbols",
                groups = {CreateAction.class, UpdateAction.class})
        String name,
        @Schema(description = "User email", example = "ivan@mail.ru")
        @NotNull(message = "Username must not be null",
                groups = {CreateAction.class, UpdateAction.class})
        @Length(max = 255,
                message = "Username length must be less than 255 symbols",
                groups = {CreateAction.class, UpdateAction.class})
        String username,
        @Schema(description = "User encrypted password",
      example = "$2a$10$imPfoeeqqf2wsrsfRs465.fZbsMmGXXpDuvxNFuy5XA5Z3yr0my56")

        // Т.е. сервер только принимает, но не отправляет пароль
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Password must not be null",
                groups = {CreateAction.class, UpdateAction.class})
        String password,
        @Schema(description = "User encrypted password confirmation",
      example = "$2a$10$imPfoeeqqf2wsrsfRs465.fZbsMmGXXpDuvxNFuy5XA5Z3yr0my56")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        // Т.е. сервер только принимает, но не отправляет confirm пароль
        @NotNull(message = "Password confirmation must not be null",
                groups = CreateAction.class)
        String passwordConfirm) {
}
