package ru.fildv.tasksjdbc.http.rest;

/*
  Для @PreAuthorize использовал бин "expressionHandler" из ApplicationConfig
  плюс @EnableGlobalMethodSecurity над конфигом
  CustomMethodSecurityExpressionRoot и CustomSecurityExpressionHandler
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fildv.tasksjdbc.database.entity.task.Task;
import ru.fildv.tasksjdbc.database.entity.task.TaskImage;
import ru.fildv.tasksjdbc.http.dto.task.TaskDto;
import ru.fildv.tasksjdbc.http.dto.task.TaskImageDto;
import ru.fildv.tasksjdbc.http.mapper.TaskImageMapper;
import ru.fildv.tasksjdbc.http.mapper.TaskMapper;
import ru.fildv.tasksjdbc.http.validation.group.UpdateAction;
import ru.fildv.tasksjdbc.service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskImageMapper taskImageMapper;

    @GetMapping("/{id}")
    @Operation(summary = "Get taskDto by id")
    @PreAuthorize("canAccessTask(#id)")
    public TaskDto getById(final @PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @PutMapping
    @Operation(summary = "Update task")
    @PreAuthorize("canAccessTask(#dto.id)")
    public TaskDto update(final @Validated(UpdateAction.class) @RequestBody
                              TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task by id")
    @PreAuthorize("canAccessTask(#id)")
    public void deleteById(final @PathVariable Long id) {
        taskService.delete(id);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to task")
    @PreAuthorize("canAccessTask(#id)")
    public void uploadImage(final @PathVariable Long id,
                            final @Validated
                            @ModelAttribute TaskImageDto imageDto) {
        TaskImage image = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, image);
    }
}
