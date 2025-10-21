package org.example.shoes.controller;

import org.example.shoes.dto.AuthDtos;
import org.example.shoes.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * chat Profile 下的轻量登录控制器：不访问数据库，仅签发 JWT。
 */
@RestController
@Profile("chat")
@RequestMapping("/api/auth")
public class ChatAuthController {

  private final JwtUtil jwtUtil;

  @Value("${app.auth.dev.accept-any-password:true}")
  private boolean devAcceptAnyPassword;

  @Value("${app.auth.dev.fixed-password:}")
  private String devFixedPassword;

  public ChatAuthController(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Validated @RequestBody AuthDtos.LoginRequest req) {
    if (!devAcceptAnyPassword) {
      if (devFixedPassword == null || devFixedPassword.isEmpty() || !devFixedPassword.equals(req.password)) {
        return ResponseEntity.status(401).body("开发模式：密码不正确");
      }
    }
    String token = jwtUtil.generateToken(req.username);
    return ResponseEntity.ok(new AuthDtos.TokenResponse(token));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@Validated @RequestBody AuthDtos.RegisterRequest req) {
    // 开发模式：不落库，直接返回 token 即可
    String token = jwtUtil.generateToken(req.username);
    return ResponseEntity.ok(new AuthDtos.TokenResponse(token));
  }
}
