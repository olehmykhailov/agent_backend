package com.example.demo.messages.datalayer.repositories;

import com.example.demo.messages.datalayer.entities.MessageEntity;
import com.example.demo.messages.datalayer.enums.SenderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface MessagesRepository extends JpaRepository<MessageEntity, UUID> {



    List<MessageForAgent> findByChat_IdOrderByCreatedAtAsc(UUID chatId);

    Page<MessageEntity> findAll(Specification<MessageEntity> spec, Pageable pageable);
}
