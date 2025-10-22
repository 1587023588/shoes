package org.example.shoes.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.example.shoes.entity.ChatConversation;
import org.example.shoes.entity.ChatMessage;
import org.example.shoes.repository.UserRepository;
import org.example.shoes.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!chat")
@RequestMapping("/api/chat")
public class ChatRestController {
    private final ChatService chatService;
    private final UserRepository userRepo;

    public ChatRestController(ChatService chatService, UserRepository userRepo) {
        this.chatService = chatService;
        this.userRepo = userRepo;
    }

    // 会话列表
    @GetMapping("/conversations")
    public List<Map<String, Object>> conversations(@AuthenticationPrincipal UserDetails principal) {
        Long uid = userRepo.findByUsername(principal.getUsername()).map(org.example.shoes.entity.User::getId)
                .orElseThrow();
        List<ChatConversation> list = chatService.listUserConversations(uid);
        List<Map<String, Object>> out = new ArrayList<>();
        for (ChatConversation c : list) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", c.getId());
            m.put("type", c.getType());
            m.put("name", c.getName());
            out.add(m);
        }
        return out;
    }

    // 创建/获取私聊会话
    @PostMapping("/conversations/dm")
    public Map<String, Object> dm(@AuthenticationPrincipal UserDetails principal,
            @RequestBody Map<String, Object> body) {
        Long fromId = userRepo.findByUsername(principal.getUsername()).map(org.example.shoes.entity.User::getId)
                .orElseThrow();
        Long toId = ((Number) body.get("userId")).longValue();
        Long cid = chatService.ensureDmConversation(fromId, toId);
        return Map.of("conversationId", cid);
    }

    // 创建群聊
    @PostMapping("/conversations/group")
    public Map<String, Object> group(@AuthenticationPrincipal UserDetails principal,
            @RequestBody Map<String, Object> body) {
        Long ownerId = userRepo.findByUsername(principal.getUsername()).map(org.example.shoes.entity.User::getId)
                .orElseThrow();
        String name = Objects.toString(body.get("name"), "");
        @SuppressWarnings("unchecked")
        List<Number> ids = (List<Number>) body.getOrDefault("memberIds", Collections.emptyList());
        List<Long> memberIds = new ArrayList<>();
        for (Number n : ids)
            memberIds.add(n.longValue());
        Long cid = chatService.createGroup(name, ownerId, memberIds);
        return Map.of("conversationId", cid);
    }

    // 拉取消息
    @GetMapping("/conversations/{id}/messages")
    public List<Map<String, Object>> messages(@AuthenticationPrincipal UserDetails principal,
            @PathVariable("id") Long conversationId,
            @RequestParam(value = "before", required = false) Instant before,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        Long uid = userRepo.findByUsername(principal.getUsername()).map(org.example.shoes.entity.User::getId)
                .orElseThrow();
        if (!chatService.isMember(conversationId, uid))
            throw new org.springframework.security.access.AccessDeniedException("not member");
        List<ChatMessage> list = chatService.getMessages(conversationId, before, Math.min(size, 100));
        List<Map<String, Object>> out = new ArrayList<>();
        for (ChatMessage m : list) {
            Map<String, Object> mm = new LinkedHashMap<>();
            mm.put("id", m.getId());
            mm.put("senderId", m.getSenderId());
            mm.put("content", m.getContent());
            mm.put("createdAt", m.getCreatedAt());
            out.add(mm);
        }
        return out;
    }

    // 退出/删除会话：群聊只有群主可彻底删除；其他情况按“退出”处理
    @DeleteMapping("/conversations/{id}")
    public Map<String, Object> deleteOrLeave(@AuthenticationPrincipal UserDetails principal,
            @PathVariable("id") Long conversationId,
            @RequestParam(value = "force", defaultValue = "false") boolean force) {
        Long uid = userRepo.findByUsername(principal.getUsername()).map(org.example.shoes.entity.User::getId)
                .orElseThrow();
        try {
            if (force) {
                chatService.deleteConversationAsOwner(conversationId, uid);
                return Map.of("status", "deleted");
            } else {
                // 默认是退出（对于 DM 等同于移除自身；若无人则清理）
                chatService.leaveConversation(conversationId, uid);
                return Map.of("status", "left");
            }
        } catch (org.springframework.security.access.AccessDeniedException ex) {
            // 如果不是群主但传了 force=true，则返回 403 由 Spring Security 处理；
            throw ex;
        }
    }
}
