package org.example.shoes.websocket;

import org.example.shoes.repository.UserRepository;
import org.example.shoes.security.JwtUtil;
import org.example.shoes.service.ChatService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Profile("!chat")
public class ChatWebSocketConfig implements WebSocketConfigurer {

  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final ChatService chatService;

  public ChatWebSocketConfig(JwtUtil jwtUtil, UserRepository userRepository, ChatService chatService) {
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
    this.chatService = chatService;
  }

  @Override
  public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
    registry.addHandler(chatWebSocketHandler(), "/ws/chat")
        .setAllowedOrigins("*");
  }

  @Bean
  public WebSocketHandler chatWebSocketHandler() {
    return new ChatWebSocketHandler(jwtUtil, userRepository, chatService);
  }
}
