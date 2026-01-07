package com.example.demo.messages.datalayer.entities;

import jakarta.persistence.*;

import lombok.*;

import java.util.UUID;

import com.example.demo.database.BaseEntity;
import com.example.demo.users.datalayer.entities.UserEntity;
import com.example.demo.chats.datalayer.entities.ChatEntity;


@Entity
@Table(name = "messages")
@Getter @Setter
public class MessageEntity extends BaseEntity {
    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;
}
