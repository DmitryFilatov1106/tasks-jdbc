package ru.fildv.tasksjdbc.http.mapper;

import org.mapstruct.Mapper;
import ru.fildv.tasksjdbc.database.entity.user.User;
import ru.fildv.tasksjdbc.http.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}
