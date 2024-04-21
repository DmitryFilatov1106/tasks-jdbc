package ru.fildv.tasksjdbc.service;

import ru.fildv.tasksjdbc.database.entity.task.Task;
import ru.fildv.tasksjdbc.database.entity.task.TaskImage;

import java.util.List;

public interface TaskService {
    Task getById(Long id);

    List<Task> getAllByUserId(Long id);

    Task update(Task task);

    Task create(Task task, Long userId);

    void delete(Long id);

    void uploadImage(Long id, TaskImage image);
}
