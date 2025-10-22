package org.example.shoes.repository;

import java.time.Instant;
import java.util.List;

import org.example.shoes.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversationIdAndCreatedAtBeforeOrderByCreatedAtDesc(Long conversationId, Instant before, Pageable pageable);
    List<ChatMessage> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);
    void deleteByConversationId(Long conversationId);
}
