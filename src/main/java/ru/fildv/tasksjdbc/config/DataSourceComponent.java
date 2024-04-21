package ru.fildv.tasksjdbc.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DataSourceComponent {
    private final JdbcTemplate jdbc;

    @SneakyThrows
    public Connection getConnection() {
        return Objects.requireNonNull(jdbc.getDataSource()).getConnection();
    }
}
