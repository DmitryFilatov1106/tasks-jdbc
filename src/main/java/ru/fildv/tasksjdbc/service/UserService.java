package ru.fildv.tasksjdbc.service;

import ru.fildv.tasksjdbc.database.entity.user.User;

public interface UserService {
    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    boolean isTaskOwner(Long userId, Long taskId);

    void delete(Long id);
}
