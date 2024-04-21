package ru.fildv.tasksjdbc.http.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fildv.tasksjdbc.database.entity.user.User;
import ru.fildv.tasksjdbc.http.dto.auth.JwtRequest;
import ru.fildv.tasksjdbc.http.dto.auth.JwtResponse;
import ru.fildv.tasksjdbc.http.dto.user.UserDto;
import ru.fildv.tasksjdbc.http.mapper.UserMapper;
import ru.fildv.tasksjdbc.http.validation.group.CreateAction;
import ru.fildv.tasksjdbc.service.AuthService;
import ru.fildv.tasksjdbc.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Auth Controller", description = "Auth API")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(final @Validated @RequestBody JwtRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public UserDto register(final @Validated(CreateAction.class) @RequestBody
                                UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User createUser = userService.create(user);
        return userMapper.toDto(createUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(final @RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
