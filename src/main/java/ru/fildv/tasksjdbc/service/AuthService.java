package ru.fildv.tasksjdbc.service;

import ru.fildv.tasksjdbc.http.dto.auth.JwtRequest;
import ru.fildv.tasksjdbc.http.dto.auth.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest jwtRequest);

    JwtResponse refresh(String refreshToken);
}
