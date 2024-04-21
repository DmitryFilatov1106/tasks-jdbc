package ru.fildv.tasksjdbc.service;

import ru.fildv.tasksjdbc.database.entity.task.TaskImage;

public interface ImageService {
    String upload(TaskImage image);
}
