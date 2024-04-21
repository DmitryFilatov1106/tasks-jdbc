package ru.fildv.tasksjdbc.database.repository.mapper;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.fildv.tasksjdbc.database.entity.task.Status;
import ru.fildv.tasksjdbc.database.entity.task.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class TaskRowMapper {
    @SneakyThrows
    public static Task mapRow(final ResultSet rs) {
        if (rs.next()) {
            Long taskId = rs.getLong("task_id");
            return getTask(rs, taskId);
        }
        return null;
    }

    @SneakyThrows
    public static List<Task> mapRows(final ResultSet rs) {
        List<Task> tasks = new ArrayList<>();
        while (rs.next()) {
            Long taskId = rs.getLong("task_id");
            if (!rs.wasNull()) {
                tasks.add(getTask(rs, taskId));
            }
        }
        return tasks;
    }

    private static Task getTask(final ResultSet rs, final Long taskId)
            throws SQLException {
        Task task = Task.builder()
                .id(taskId)
                .title(rs.getString("task_title"))
                .description(rs.getString("task_description"))
                .status(Status.valueOf(rs.getString("task_status")))
                .build();
        Timestamp timestamp = rs.getTimestamp("task_expiration_date");
        if (timestamp != null) {
            task.setExpirationDate(timestamp.toLocalDateTime());
        }
        return task;
    }
}
