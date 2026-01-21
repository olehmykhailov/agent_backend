package com.example.demo.chats.datalayer.repositories;

import com.example.demo.chats.datalayer.entities.ChatEntity;

import com.example.demo.messages.datalayer.entities.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;


public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {
    ChatEntity getChatById(UUID id);
    List<ChatEntity> findAllChatsByUserId(UUID userId);
    Page<ChatEntity> findAllByUserIdOrderByUpdatedAtDesc(UUID userId, Pageable pageable);
}
