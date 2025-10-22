package org.example.shoes.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("ws_disabled")
public class ChatWebSocketHandler extends TextWebSocketHandler {
  private static final Logger log = LoggerFactory.getLogger(ChatWebSocketHandler.class);
  private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessions.add(session);
    log.info("WS connected: {}", session.getId());
    session.sendMessage(new TextMessage("[server] connected: " + session.getId()));
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    String payload = message.getPayload();
    log.info("WS recv {}: {}", session.getId(), payload);
    // 简单群发
    for (WebSocketSession s : sessions) {
      if (s.isOpen()) {
        s.sendMessage(new TextMessage("[" + session.getId() + "] " + payload));
      }
    }
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    sessions.remove(session);
    log.info("WS closed: {} {}", session.getId(), status);
  }
}
