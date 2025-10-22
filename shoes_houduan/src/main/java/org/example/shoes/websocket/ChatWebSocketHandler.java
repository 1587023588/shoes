package org.example.shoes.websocket;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.example.shoes.entity.User;
import org.example.shoes.repository.UserRepository;
import org.example.shoes.security.JwtUtil;
import org.example.shoes.service.ChatService;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 简易群聊 WebSocket 处理器（按房间广播）。
 * 客户端连接示例：ws://host:8080/ws/chat?room=public&user=Alice
 */
public class ChatWebSocketHandler extends TextWebSocketHandler {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
  private final Map<String, Deque<Map<String, Object>>> histories = new ConcurrentHashMap<>();
  private static final int HISTORY_LIMIT = 50;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepo;
  private final ChatService chatService;

  public ChatWebSocketHandler(JwtUtil jwtUtil, UserRepository userRepo, ChatService chatService) {
    this.jwtUtil = jwtUtil;
    this.userRepo = userRepo;
    this.chatService = chatService;
  }

  @Override
  public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
    Map<String, String> qp = parseQueryParams(session.getUri());
    String room = Optional.ofNullable(qp.get("room")).orElse("public");
    String user = Optional.ofNullable(qp.get("user")).orElse("Anonymous");
    Long userId = null;

    // 可选：从 token 中解析用户名（优先于 query user）
    String token = qp.get("token");
    if (token != null && !token.isBlank()) {
      try {
        String u = jwtUtil.extractUsername(token);
        if (u != null && !u.isBlank()) {
          user = u;
          userId = userRepo.findByUsername(u).map(User::getId).orElse(null);
        }
      } catch (Exception ignored) {
        // token 非法时忽略，继续匿名/明文用户名
      }
    }

    // 如果 room 为数字（会话ID），则需要登录并校验成员资格
    Long convId = parseLongOrNull(room);
    if (convId != null) {
      if (userId == null) {
        session.close(CloseStatus.POLICY_VIOLATION.withReason("AUTH_REQUIRED"));
        return;
      }
      if (!chatService.isMember(convId, userId)) {
        session.close(CloseStatus.POLICY_VIOLATION.withReason("NOT_MEMBER"));
        return;
      }
      // 将 room 归一化为 conv:{id}
      room = "conv:" + convId;
    }

    session.getAttributes().put("room", room);
    session.getAttributes().put("user", user);

    rooms.computeIfAbsent(room, k -> new CopyOnWriteArraySet<>()).add(session);

    // 回放历史
    sendHistory(room, session);

    // 广播系统消息：加入
    Map<String, Object> join = systemMessage(room, user + " 加入了房间");
    addToHistory(room, join);
    broadcast(room, join);
  }

  @Override
  protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
    String room = (String) session.getAttributes().get("room");
    String user = (String) session.getAttributes().get("user");
    Long convId = normalizeConvId(room);
    Long userId = null;
    // 若是会话消息，尝试从用户名解析 id（仅在 token 期间已缓存成功）
    if (convId != null) {
      userId = userRepo.findByUsername(user).map(User::getId).orElse(null);
    }
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
      Map<String, Object> um = userMessage(room, user, content);
      if (convId != null && userId != null) {
        // 持久化
        try { chatService.saveMessage(convId, userId, content); } catch (Exception ignored) {}
      }
      addToHistory(room, um);
      broadcast(room, um);
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
      Map<String, Object> leave = systemMessage(room, (user != null ? user : "Anonymous") + " 离开了房间");
      addToHistory(room, leave);
      broadcast(room, leave);
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

  private void sendHistory(String room, WebSocketSession session) {
    Deque<Map<String, Object>> q = histories.get(room);
    if (q == null || q.isEmpty()) return;
    for (Map<String, Object> m : q) {
      try {
        session.sendMessage(new TextMessage(MAPPER.writeValueAsString(m)));
      } catch (IOException ignored) {
      }
    }
  }

  private void addToHistory(String room, Map<String, Object> message) {
    Deque<Map<String, Object>> q = histories.computeIfAbsent(room, k -> new ArrayDeque<>(HISTORY_LIMIT));
    if (q.size() >= HISTORY_LIMIT) {
      q.pollFirst();
    }
    q.offerLast(new LinkedHashMap<>(message));
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

  private Long parseLongOrNull(String s) {
    if (s == null || s.isEmpty()) return null;
    long result = 0L;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c < '0' || c > '9') return null;
      result = result * 10 + (c - '0');
    }
    return result;
  }

  private Long normalizeConvId(String room) {
    if (room == null) return null;
    if (room.startsWith("conv:")) {
      String id = room.substring(5);
      return parseLongOrNull(id);
    }
    return null;
  }
}
