package com.example.demo.responses.datalayer.entities;

import com.example.demo.chats.datalayer.entities.ChatEntity;
import jakarta.persistence.*;

import lombok.*;

import com.example.demo.database.BaseEntity;
import com.example.demo.messages.datalayer.entities.MessageEntity;

@Entity
@Table(name = "responses")
@Getter @Setter
public class ResponseEntity extends BaseEntity{
    private String content;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id", referencedColumnName = "id")
    private MessageEntity message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;
}
