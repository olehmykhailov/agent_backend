package com.example.demo.messages.datalayer.repositories;

import com.example.demo.messages.datalayer.entities.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MessagesRepository extends JpaRepository<MessageEntity, UUID> {
    List<MessageEntity> getMessagesByChatId(UUID chatId);

    Page<MessageEntity> getMessagesByChatId(UUID chatId, Pageable pageable);

    MessageEntity getMessageById(UUID id);
}
