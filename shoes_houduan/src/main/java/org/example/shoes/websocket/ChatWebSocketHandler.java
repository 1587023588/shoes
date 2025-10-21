package org.example.shoes.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 简易群聊 WebSocket 处理器（按房间广播）。
 * 客户端连接示例：ws://host:8080/ws/chat?room=public&user=Alice
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();

  @Override
  public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
    Map<String, String> qp = parseQueryParams(session.getUri());
    String room = Optional.ofNullable(qp.get("room")).orElse("public");
    String user = Optional.ofNullable(qp.get("user")).orElse("Anonymous");

    session.getAttributes().put("room", room);
    session.getAttributes().put("user", user);

    rooms.computeIfAbsent(room, k -> new CopyOnWriteArraySet<>()).add(session);

    // 广播系统消息：加入
    broadcast(room, systemMessage(room, user + " 加入了房间"));
  }

  @Override
  protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
    String room = (String) session.getAttributes().get("room");
    String user = (String) session.getAttributes().get("user");
    if (room == null)
      room = "public";
    if (user == null)
      user = "Anonymous";

    String payload = message.getPayload();
    Map<String, Object> msg;
    try {
      @SuppressWarnings("unchecked")
      Map<String, Object> parsed = MAPPER.readValue(payload, Map.class);
      msg = parsed;
    } catch (JsonProcessingException e) {
      // 兼容纯文本消息
      msg = new HashMap<>();
      msg.put("type", "message");
      msg.put("content", payload);
    }
    String type = Objects.toString(msg.getOrDefault("type", "message"));
    if ("message".equalsIgnoreCase(type)) {
      String content = Objects.toString(msg.getOrDefault("content", ""));
      broadcast(room, userMessage(room, user, content));
    }
  }

  @Override
  public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
    String room = (String) session.getAttributes().get("room");
    String user = (String) session.getAttributes().get("user");
    if (room != null) {
      Set<WebSocketSession> set = rooms.get(room);
      if (set != null) {
        set.remove(session);
        if (set.isEmpty()) {
          rooms.remove(room);
        }
      }
      broadcast(room, systemMessage(room, (user != null ? user : "Anonymous") + " 离开了房间"));
    }
  }

  private void broadcast(String room, Map<String, Object> message) {
    Set<WebSocketSession> set = rooms.get(room);
    if (set == null || set.isEmpty())
      return;
    String json;
    try {
      json = MAPPER.writeValueAsString(message);
    } catch (JsonProcessingException e) {
      return;
    }
    for (WebSocketSession s : set) {
      if (s.isOpen()) {
        try {
          s.sendMessage(new TextMessage(json));
        } catch (IOException ignored) {
        }
      }
    }
  }

  private Map<String, Object> systemMessage(String room, String content) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("type", "system");
    m.put("room", room);
    m.put("timestamp", Instant.now().toString());
    m.put("content", content);
    return m;
  }

  private Map<String, Object> userMessage(String room, String user, String content) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("type", "message");
    m.put("room", room);
    m.put("user", user);
    m.put("timestamp", Instant.now().toString());
    m.put("content", content);
    return m;
  }

  private Map<String, String> parseQueryParams(URI uri) {
    Map<String, String> map = new HashMap<>();
    if (uri == null)
      return map;
    String q = uri.getQuery();
    if (q == null || q.isEmpty())
      return map;
    for (String part : q.split("&")) {
      int idx = part.indexOf('=');
      if (idx > 0) {
        String k = URLDecoder.decode(part.substring(0, idx), StandardCharsets.UTF_8);
        String v = URLDecoder.decode(part.substring(idx + 1), StandardCharsets.UTF_8);
        map.put(k, v);
      }
    }
    return map;
  }
}
