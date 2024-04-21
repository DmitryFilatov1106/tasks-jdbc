package ru.fildv.tasksjdbc.database.repository.mapper;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.fildv.tasksjdbc.database.entity.task.Task;
import ru.fildv.tasksjdbc.database.entity.user.Role;
import ru.fildv.tasksjdbc.database.entity.user.User;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class UserRowMapper {
    @SneakyThrows
    public static User mapRow(final ResultSet rs) {
        Set<Role> roles = new HashSet<>();
        while (rs.next()) {
            roles.add(Role.valueOf(rs.getString("user_role_role")));
        }
        rs.beforeFirst();
        List<Task> tasks = TaskRowMapper.mapRows(rs);
        rs.beforeFirst();
        if (rs.next()) {
            return User.builder()
                    .id(rs.getLong("user_id"))
                    .name(rs.getString("user_name"))
                    .username(rs.getString("user_username"))
                    .password(rs.getString("user_password"))
                    .roles(roles)
                    .tasks(tasks)
                    .build();
        }
        return null;
    }
}
