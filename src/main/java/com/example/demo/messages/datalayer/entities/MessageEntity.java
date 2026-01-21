package com.example.demo.messages.datalayer.entities;

import jakarta.persistence.*;

import lombok.*;


import com.example.demo.amq.dtos.response.toolcall.ToolCallDto;
import com.example.demo.database.BaseEntity;
import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.messages.datalayer.enums.SenderType;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "messages")
@Getter @Setter
public class MessageEntity extends BaseEntity {
    @Column
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SenderType role;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<ToolCallDto> toolCalls;

    @Column
    private String toolCallId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatEntity chat;
}
