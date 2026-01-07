package com.example.demo.responses.datalayer.repositories;

import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.responses.datalayer.entities.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ResponseRepository extends CrudRepository<ResponseEntity, UUID> {
    Page<ResponseEntity> findAllByChatIdOrderByUpdatedAtDesc(UUID chatId, Pageable pageable);
}
