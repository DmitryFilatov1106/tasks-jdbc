package ru.fildv.tasksjdbc.database.entity.task;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Task implements Serializable {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime expirationDate;

    @Builder.Default
    private List<TaskImage> images = new ArrayList<>();
}
