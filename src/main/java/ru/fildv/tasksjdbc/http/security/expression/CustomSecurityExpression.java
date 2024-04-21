package ru.fildv.tasksjdbc.http.security.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fildv.tasksjdbc.database.entity.user.Role;
import ru.fildv.tasksjdbc.http.security.JwtEntity;
import ru.fildv.tasksjdbc.service.UserService;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final UserService userService;

    public boolean canAccessUser(final Long userId) {
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long id = user.id();
        return id.equals(userId) || hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    public boolean canAccessTask(final Long taskId) {
        Authentication authentication
                = SecurityContextHolder.getContext().getAuthentication();
        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long id = user.id();
        return userService.isTaskOwner(id, taskId);
    }

    private boolean hasAnyRole(final Authentication authentication,
                               final Role... roles) {
        for (Role role : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                    role.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }
}
