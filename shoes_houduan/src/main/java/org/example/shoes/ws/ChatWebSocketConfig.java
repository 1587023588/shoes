package org.example.shoes.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@Profile("ws_disabled")
public class ChatWebSocketConfig implements WebSocketConfigurer {

  private final ChatWebSocketHandler handler;

  public ChatWebSocketConfig(ChatWebSocketHandler handler) {
    this.handler = handler;
  }

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    // 简单回声/广播通道：ws://<host>:<port>/ws/chat
    registry.addHandler(handler, "/ws/chat").setAllowedOrigins("*");
  }
}
