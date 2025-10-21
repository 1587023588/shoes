package org.example.shoes.controller;

import org.example.shoes.dto.AuthDtos;
import org.example.shoes.entity.User;
import org.example.shoes.repository.UserRepository;
import org.example.shoes.security.JwtUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!chat")
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    @Value("${app.auth.dev.enabled:false}")
    private boolean devAuthEnabled;
    @Value("${app.auth.dev.accept-any-password:true}")
    private boolean devAcceptAnyPassword;
    @Value("${app.auth.dev.fixed-password:}")
    private String devFixedPassword;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody AuthDtos.RegisterRequest req) {
        if (userRepository.existsByUsername(req.username)) {
            return ResponseEntity.badRequest().body("用户名已存在");
        }
        User u = new User();
        u.setUsername(req.username);
        u.setPasswordHash(passwordEncoder.encode(req.password));
        userRepository.save(u);
        String token = jwtUtil.generateToken(u.getUsername());
        return ResponseEntity.ok(new AuthDtos.TokenResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody AuthDtos.LoginRequest req) {
        if (devAuthEnabled) {
            // 开发模式：跳过数据库与认证管理器
            if (!devAcceptAnyPassword) {
                if (devFixedPassword == null || devFixedPassword.isEmpty() || !devFixedPassword.equals(req.password)) {
                    return ResponseEntity.status(401).body("开发模式：密码不正确");
                }
            }
            String token = jwtUtil.generateToken(req.username);
            return ResponseEntity.ok(new AuthDtos.TokenResponse(token));
        } else {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.username, req.password));
            String token = jwtUtil.generateToken(req.username);
            return ResponseEntity.ok(new AuthDtos.TokenResponse(token));
        }
    }
}
