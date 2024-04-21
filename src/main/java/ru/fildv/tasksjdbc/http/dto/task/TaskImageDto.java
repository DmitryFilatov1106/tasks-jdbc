package ru.fildv.tasksjdbc.http.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Task Image DTO")
public record TaskImageDto(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String image,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Image must be not null.")
        MultipartFile file) {
}
