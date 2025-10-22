package org.example.shoes.service;

import java.time.Instant;
import java.util.List;

import org.example.shoes.entity.ChatConversation;

public interface ChatService {
    Long ensureDmConversation(Long userId1, Long userId2);
    Long createGroup(String name, Long ownerId, List<Long> memberIds);
    boolean isMember(Long conversationId, Long userId);
    org.example.shoes.entity.ChatMessage saveMessage(Long conversationId, Long senderId, String content);
    java.util.List<org.example.shoes.entity.ChatMessage> getMessages(Long conversationId, Instant before, int size);
    List<ChatConversation> listUserConversations(Long userId);

    /**
     * 当前用户退出会话；若会话无人或仅剩<=0人，执行清理；DM 会话在任一方退出后若无其他成员可清除。
     */
    void leaveConversation(Long conversationId, Long userId);

    /**
     * 作为群主删除整个会话（群聊）；DM 不允许直接删除，仅可 leave。若非群主调用则抛出 AccessDenied。
     */
    void deleteConversationAsOwner(Long conversationId, Long operatorUserId);
}
