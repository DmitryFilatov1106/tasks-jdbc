package ru.fildv.tasksjdbc.http.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.fildv.tasksjdbc.database.entity.task.Status;
import ru.fildv.tasksjdbc.http.validation.group.CreateAction;
import ru.fildv.tasksjdbc.http.validation.group.UpdateAction;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Task DTO")
public record TaskDto(
        @NotNull(message = "ID must not be null", groups = UpdateAction.class)
        Long id,
        @NotNull(message = "Title must not be null",
                groups = {CreateAction.class, UpdateAction.class})
        @Length(max = 255,
                message = "Title length must be less than 255 symbols",
                groups = {CreateAction.class, UpdateAction.class})
        String title,
        @Length(max = 255,
                message = "Description must be less than 255 symbols",
                groups = {CreateAction.class, UpdateAction.class})
        String description,
        Status status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime expirationDate,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        List<TaskImageDto> images) {
}
