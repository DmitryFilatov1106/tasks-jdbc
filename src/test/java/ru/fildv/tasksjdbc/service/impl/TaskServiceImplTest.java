package ru.fildv.tasksjdbc.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.fildv.tasksjdbc.config.TestConfig;
import ru.fildv.tasksjdbc.database.entity.task.Status;
import ru.fildv.tasksjdbc.database.entity.task.Task;
import ru.fildv.tasksjdbc.database.entity.task.TaskImage;
import ru.fildv.tasksjdbc.database.repository.TaskRepository;
import ru.fildv.tasksjdbc.exception.ResourceNotFoundException;
import ru.fildv.tasksjdbc.service.ImageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        Long id = 1L;
        Task task = Task.builder()
                .id(id)
                .build();
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.getById(id);
        Mockito.verify(taskRepository).findById(id);
        assertEquals(task, testTask);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(Task.builder().build());
        }
        Mockito.when(taskRepository.findAllByUserId(userId))
                .thenReturn(tasks);
        List<Task> testTasks = taskService.getAllByUserId(userId);
        Mockito.verify(taskRepository).findAllByUserId(userId);
        assertEquals(tasks, testTasks);
    }

//    @Test
//    void getSoonTasks() {
//        Duration duration = Duration.ofHours(1);
//        List<Task> tasks = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            tasks.add(Task.builder().build());
//        }
//        Mockito.when(taskRepository.findAllSoonTasks(Mockito.any(), Mockito.any()))
//                .thenReturn(tasks);
//        List<Task> testTasks = taskService.getAllSoonTasks(duration);
//        Mockito.verify(taskRepository)
//                .findAllSoonTasks(Mockito.any(), Mockito.any());
//        Assertions.assertEquals(tasks, testTasks);
//    }

    @Test
    void update() {
        Long id = 1L;
        Task task = Task.builder().build();
        task.setId(id);
        task.setStatus(Status.DONE);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).update(task);
        assertEquals(task, testTask);
        assertEquals(task.getTitle(), testTask.getTitle());
        assertEquals(
                task.getDescription(),
                testTask.getDescription()
        );
        assertEquals(task.getStatus(), testTask.getStatus());
    }

    @Test
    void updateWithEmptyStatus() {
        Long id = 1L;
        Task task = Task.builder().build();
        task.setId(id);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).update(task);
        assertEquals(task.getTitle(), testTask.getTitle());
        assertEquals(
                task.getDescription(),
                testTask.getDescription()
        );
        assertEquals(testTask.getStatus(), Status.TODO);
    }

    @Test
    void create() {
        Long userId = 1L;
        Long taskId = 1L;
        Task task = Task.builder().build();
        Mockito.doAnswer(invocation -> {
                    Task savedTask = invocation.getArgument(0);
                    savedTask.setId(taskId);
                    return savedTask;
                })
                .when(taskRepository).create(task);
        Task testTask = taskService.create(task, userId);
        Mockito.verify(taskRepository).create(task);
        Assertions.assertNotNull(testTask.getId());
        Mockito.verify(taskRepository).assignToUserById(userId, task.getId());
    }

    @Test
    void delete() {
        Long id = 1L;
        taskService.delete(id);
        Mockito.verify(taskRepository).delete(id);
    }

    @Test
    void uploadImage() {
        Long id = 1L;
        String imageName = "imageName";
        TaskImage taskImage = TaskImage.builder().build();
        Mockito.when(imageService.upload(taskImage))
                .thenReturn(imageName);
        taskService.uploadImage(id, taskImage);
        Mockito.verify(taskRepository).addImage(id, imageName);
    }
}
