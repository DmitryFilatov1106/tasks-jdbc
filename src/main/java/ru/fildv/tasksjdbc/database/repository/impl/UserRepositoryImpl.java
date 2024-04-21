package ru.fildv.tasksjdbc.database.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.fildv.tasksjdbc.config.DataSourceComponent;
import ru.fildv.tasksjdbc.database.entity.user.Role;
import ru.fildv.tasksjdbc.database.entity.user.User;
import ru.fildv.tasksjdbc.database.repository.UserRepository;
import ru.fildv.tasksjdbc.database.repository.mapper.UserRowMapper;
import ru.fildv.tasksjdbc.exception.ResourceMappingException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final DataSourceComponent dataSource;
    private static final String FIND_BY_ID = """
            SELECT u.id              as user_id,
                   u.name            as user_name,
                   u.username        as user_username,
                   u.password        as user_password,
                   ur.role           as user_role_role,
                   t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM users u
                     LEFT JOIN users_roles ur on u.id = ur.user_id
                     LEFT JOIN users_tasks ut on u.id = ut.user_id
                     LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.id = ?
            """;

    private static final String FIND_BY_USERNAME = """
            SELECT u.id              as user_id,
                   u.name            as user_name,
                   u.username        as user_username,
                   u.password        as user_password,
                   ur.role           as user_role_role,
                   t.id              as task_id,
                   t.title           as task_title,
                   t.description     as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status          as task_status
            FROM users u
                     LEFT JOIN users_roles ur on u.id = ur.user_id
                     LEFT JOIN users_tasks ut on u.id = ut.user_id
                     LEFT JOIN tasks t on ut.task_id = t.id
            WHERE u.username = ?
            """;
    private static final String UPDATE = """
            UPDATE users
            SET name     = ?,
                username = ?,
                password = ?
            WHERE id = ?
            """;
    private static final String CREATE = """
            INSERT INTO users
                (name, username, password)
            VALUES (?, ?, ?)
            """;
    private static final String DELETE = """
            DELETE
            FROM users
            WHERE id = ?
            """;
    private static final String INSERT_USER_ROLE = """
            INSERT INTO users_roles
                (user_id, role)
            VALUES (?, ?)
            """;
    private static final String IS_TASK_OWNER = """
            SELECT exists(
                SELECT 1
                FROM users_tasks
                WHERE user_id = ?
                AND task_id = ?)
            """;

    @Override
    public Optional<User> findById(final Long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     FIND_BY_ID,
                     ResultSet.TYPE_SCROLL_SENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while finding user by id.");
        }
    }

    @Override
    public Optional<User> findByUsername(final String username) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     FIND_BY_USERNAME,
                     ResultSet.TYPE_SCROLL_SENSITIVE,
                     ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while finding user by username.");
        }
    }

    @Override
    public void update(final User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(UPDATE)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while updating user.");
        }
    }

    @Override
    public void create(final User user) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     CREATE,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                rs.next();
                user.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new ResourceMappingException("Error while creating user.");
        }
    }

    @Override
    public void insertUserRole(final Long userId, final Role role) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(INSERT_USER_ROLE)) {
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while inserting user role.");
        }
    }

    @Override
    public boolean isTaskOwner(final Long userId, final Long taskId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement
                     = connection.prepareStatement(IS_TASK_OWNER)) {
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new ResourceMappingException(
                    "Error while checking owner of task.");
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
            throw new ResourceMappingException("Error while deleting user.");
        }
    }
}
