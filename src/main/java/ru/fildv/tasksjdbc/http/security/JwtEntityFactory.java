package ru.fildv.tasksjdbc.http.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.fildv.tasksjdbc.database.entity.user.Role;
import ru.fildv.tasksjdbc.database.entity.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtEntityFactory {
    public static JwtEntity create(final User user) {
        return JwtEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .password(user.getPassword())
                .authorities(mapToGrantedAuthorities(
                        new ArrayList<>(user.getRoles())))
                .build();
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(
            final List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

    }
}
