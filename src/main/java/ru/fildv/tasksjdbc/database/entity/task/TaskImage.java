package ru.fildv.tasksjdbc.database.entity.task;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
public class TaskImage implements Serializable {
    private String image;
    private transient MultipartFile file;
}
