package com.example.demo.chats.datalayer.entities;


import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.database.BaseEntity;
import com.example.demo.users.datalayer.entities.UserEntity;
import com.example.demo.messages.datalayer.entities.MessageEntity;


@Entity
@Table(name = "chats")
@Getter @Setter
public class ChatEntity extends BaseEntity{

    private String title;
    @Column(name = "title_generated", nullable = false)
    private boolean titleGenerated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageEntity> messages = new ArrayList<>();
}
