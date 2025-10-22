package org.example.shoes.repository;

import java.util.List;
import java.util.Optional;

import org.example.shoes.entity.ChatConversationMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatConversationMemberRepository extends JpaRepository<ChatConversationMember, Long> {
    List<ChatConversationMember> findByConversationId(Long conversationId);
    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);
    Optional<ChatConversationMember> findByConversationIdAndUserId(Long conversationId, Long userId);
    List<ChatConversationMember> findByUserId(Long userId);
    long countByConversationId(Long conversationId);
    void deleteByConversationIdAndUserId(Long conversationId, Long userId);
    void deleteByConversationId(Long conversationId);
}
