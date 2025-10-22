package org.example.shoes.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.example.shoes.entity.ChatConversation;
import org.example.shoes.entity.ChatConversationMember;
import org.example.shoes.entity.ChatMessage;
import org.example.shoes.repository.ChatConversationMemberRepository;
import org.example.shoes.repository.ChatConversationRepository;
import org.example.shoes.repository.ChatMessageRepository;
import org.example.shoes.service.ChatService;
import org.springframework.data.domain.PageRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Profile("!chat")
public class ChatServiceImpl implements ChatService {
    private final ChatConversationRepository convRepo;
    private final ChatConversationMemberRepository memberRepo;
    private final ChatMessageRepository msgRepo;

    public ChatServiceImpl(ChatConversationRepository convRepo,
            ChatConversationMemberRepository memberRepo,
            ChatMessageRepository msgRepo) {
        this.convRepo = convRepo;
        this.memberRepo = memberRepo;
        this.msgRepo = msgRepo;
    }

    @Override
    @Transactional
    public Long ensureDmConversation(Long userId1, Long userId2) {
        // 查找是否存在双方都是成员且类型为 DM 的会话；简单实现：扫描 userId1 的会话找与 userId2 共有的 DM
        List<ChatConversationMember> m1 = memberRepo.findByUserId(userId1);
        Set<Long> convIds = new HashSet<>();
        for (ChatConversationMember m : m1)
            convIds.add(m.getConversationId());
        for (Long cid : convIds) {
            Optional<ChatConversation> copt = convRepo.findById(cid);
            if (copt.isPresent() && copt.get().getType() == ChatConversation.Type.DM) {
                if (memberRepo.existsByConversationIdAndUserId(cid, userId2)) {
                    return cid;
                }
            }
        }
        // 不存在则创建
        ChatConversation c = new ChatConversation();
        c.setType(ChatConversation.Type.DM);
        convRepo.save(c);
        ChatConversationMember a = new ChatConversationMember();
        a.setConversationId(c.getId());
        a.setUserId(userId1);
        a.setRole(ChatConversationMember.Role.MEMBER);
        memberRepo.save(a);
        ChatConversationMember b = new ChatConversationMember();
        b.setConversationId(c.getId());
        b.setUserId(userId2);
        b.setRole(ChatConversationMember.Role.MEMBER);
        memberRepo.save(b);
        return c.getId();
    }

    @Override
    @Transactional
    public Long createGroup(String name, Long ownerId, List<Long> memberIds) {
        ChatConversation c = new ChatConversation();
        c.setType(ChatConversation.Type.GROUP);
        c.setName(name);
        convRepo.save(c);
        // 所有成员 + owner
        Set<Long> all = new LinkedHashSet<>();
        all.add(ownerId);
        if (memberIds != null)
            all.addAll(memberIds);
        for (Long uid : all) {
            ChatConversationMember m = new ChatConversationMember();
            m.setConversationId(c.getId());
            m.setUserId(uid);
            m.setRole(Objects.equals(uid, ownerId) ? ChatConversationMember.Role.OWNER
                    : ChatConversationMember.Role.MEMBER);
            memberRepo.save(m);
        }
        return c.getId();
    }

    @Override
    public boolean isMember(Long conversationId, Long userId) {
        return memberRepo.existsByConversationIdAndUserId(conversationId, userId);
    }

    @Override
    @Transactional
    public ChatMessage saveMessage(Long conversationId, Long senderId, String content) {
        ChatMessage m = new ChatMessage();
        m.setConversationId(conversationId);
        m.setSenderId(senderId);
        m.setContent(content);
        return msgRepo.save(m);
    }

    @Override
    public List<ChatMessage> getMessages(Long conversationId, Instant before, int size) {
        if (before != null) {
            return msgRepo.findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDesc(conversationId, before,
                    PageRequest.of(0, size));
        }
        return msgRepo.findByConversationIdOrderByCreatedAtDesc(conversationId, PageRequest.of(0, size));
    }

    @Override
    public List<ChatConversation> listUserConversations(Long userId) {
        List<ChatConversationMember> ms = memberRepo.findByUserId(userId);
        List<ChatConversation> out = new ArrayList<>();
        for (ChatConversationMember m : ms) {
            convRepo.findById(m.getConversationId()).ifPresent(out::add);
        }
        // 简单返回，客户端可再拉取最后一条消息
        return out;
    }

    @Override
    @Transactional
    public void leaveConversation(Long conversationId, Long userId) {
        memberRepo.deleteByConversationIdAndUserId(conversationId, userId);
        long left = memberRepo.countByConversationId(conversationId);
        if (left <= 0) {
            // 无成员，彻底清理
            msgRepo.deleteByConversationId(conversationId);
            convRepo.deleteById(conversationId);
        }
    }

    @Override
    @Transactional
    public void deleteConversationAsOwner(Long conversationId, Long operatorUserId) {
        ChatConversation conversation = convRepo.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("conversation not found"));
        ChatConversationMember cm = memberRepo.findByConversationIdAndUserId(conversationId, operatorUserId)
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("not member"));
        if (conversation.getType() != ChatConversation.Type.GROUP
                || cm.getRole() != ChatConversationMember.Role.OWNER) {
            throw new org.springframework.security.access.AccessDeniedException("only owner can delete group");
        }
        // 物理删除：消息->成员->会话
        msgRepo.deleteByConversationId(conversationId);
        memberRepo.deleteByConversationId(conversationId);
        convRepo.deleteById(conversationId);
    }
}
