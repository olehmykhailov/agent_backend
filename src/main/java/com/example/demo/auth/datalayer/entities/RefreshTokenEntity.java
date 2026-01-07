package com.example.demo.auth.datalayer.entities;


import com.example.demo.users.datalayer.entities.UserEntity;
import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
@Getter @Setter
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;


    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

    private boolean revoked;

}
