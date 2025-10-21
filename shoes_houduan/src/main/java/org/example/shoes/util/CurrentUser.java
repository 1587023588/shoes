package org.example.shoes.util;

import org.example.shoes.entity.User;
import org.example.shoes.repository.UserRepository;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CurrentUser {
    private final UserRepository userRepository;

    public CurrentUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "未登录");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "用户不存在"));
    }
}
