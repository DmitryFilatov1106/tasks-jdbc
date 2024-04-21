package ru.fildv.tasksjdbc.database.entity.user;

import lombok.Builder;
import lombok.Data;
import ru.fildv.tasksjdbc.database.entity.task.Task;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class User implements Serializable {
    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirm;
    private Set<Role> roles;
    private List<Task> tasks;
}
