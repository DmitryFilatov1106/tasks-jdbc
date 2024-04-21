package ru.fildv.tasksjdbc.http.mapper;

import org.mapstruct.Mapper;
import ru.fildv.tasksjdbc.database.entity.task.Task;
import ru.fildv.tasksjdbc.http.dto.task.TaskDto;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}
