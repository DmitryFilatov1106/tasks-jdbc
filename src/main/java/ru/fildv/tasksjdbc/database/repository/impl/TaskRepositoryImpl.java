package ru.fildv.tasksjdbc.database.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.fildv.tasksjdbc.config.DataSourceComponent;
import ru.fildv.tasksjdbc.database.entity.task.Task;
import ru.fildv.tasksjdbc.database.entity.task.TaskImage;
import ru.fildv.tasksjdbc.database.repository.TaskRepository;
import ru.fildv.tasksjdbc.database.repository.mapper.TaskRowMapper;
import ru.fildv.tasksjdbc.exception.ResourceMappingException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    private final DataSourceComponent dataSource;
    private static final String FIND_BY_ID = """
            SELECT
                t.id as task_id,
                t.title as task_title,
                t.description as task_description,
                t.expiration_date as task_expiration_date,
                t.status as task_status
            FROM tasks t
            WHERE t.id = ?
            """;

    private static final String FIND_IMAGES_BY_ID = """
            SELECT
                ti.image as task_image
            FROM tasks_images ti
            WHERE ti.task_id = ?
            """;

    private static final String FIND_ALL_BY_USER_ID = """
            SELECT
                t.id as task_id,
                t.title as task_title,
                t.description as task_description,
                t.expiration_date as task_expiration_date,
                t.status as task_status
            FROM tasks t
                JOIN users_tasks ut on t.id = ut.task_id
            WHERE ut.user_id = ?
            """;
    private static final String ASSIGN = """
            INSERT INTO users_tasks
                (task_id, user_id)
            VALUES (?, ?)
            """;
    private static final String DELETE = """
            DELETE from tasks
            WHERE id = ?
            """;
    private static final String UPDATE = """
            UPDATE tasks
            SET title = ?,
                description = ?,
                expiration_date = ?,
                status = ?
            WHERE id = ?
            """;
    private static final String CREATE = """
            INSERT INTO tasks
                    (title, description, expiration_date, status)
            VALUES (?,?,?,?)
                    """;

    private static final String CREATE_IMAGE = """
            INSERT INTO tasks_images
            (task_id, image)
            VALUES (?,?)
            """;

    @Override
    public Optional<Task> findById(final Long id) {
        Task task = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(FIND_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                task = TaskRowMapper.mapRow(rs);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while finding by user id");
        }
        if (task != null) {
            List<TaskImage> images = findImagesById(id);
            task.setImages(images);
        }
        return Optional.ofNullable(task);
    }

    @Override
    public List<Task> findAllByUserId(final Long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(FIND_ALL_BY_USER_ID)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                return TaskRowMapper.mapRows(rs);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while finding all by user id.");
        }
    }

    @Override
    public void assignToUserById(final Long taskId, final Long userId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(ASSIGN)) {
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while assigning to user.");
        }
    }

    @Override
    public void update(final Task task) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(UPDATE)) {
            statement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3,
                        Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.setLong(5, task.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating task.");
        }
    }

    @Override
    public void create(final Task task) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(
                             CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, task.getTitle());
            if (task.getDescription() == null) {
                statement.setNull(2, Types.VARCHAR);
            } else {
                statement.setString(2, task.getDescription());
            }
            if (task.getExpirationDate() == null) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3,
                        Timestamp.valueOf(task.getExpirationDate()));
            }
            statement.setString(4, task.getStatus().name());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                task.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating task.");
        }
    }

    @Override
    public void delete(final Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(DELETE)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while deleting task.");
        }
    }

    @Override
    public void addImage(final Long taskId, final String fileName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(CREATE_IMAGE)) {
            statement.setLong(1, taskId);
            statement.setString(2, fileName);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating image.");
        }
    }

    private List<TaskImage> findImagesById(final Long id) {
        List<TaskImage> images = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(FIND_IMAGES_BY_ID)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    TaskImage taskImage = TaskImage.builder()
                            .image(rs.getString("task_image"))
                            .build();
                    images.add(taskImage);
                }
            }
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while finding all by user id.");
        }
        return images;
    }
}
