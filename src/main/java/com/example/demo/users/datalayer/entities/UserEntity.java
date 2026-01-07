package com.example.demo.users.datalayer.entities;

import com.example.demo.database.BaseEntity;
import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.List;

import com.example.demo.chats.datalayer.entities.ChatEntity;
import com.example.demo.messages.datalayer.entities.MessageEntity;

@Entity
@Table(name = "users")
@Getter @Setter
public class UserEntity extends BaseEntity {

    private String email;
    private String password;
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatEntity> chats = new ArrayList<>();

}
