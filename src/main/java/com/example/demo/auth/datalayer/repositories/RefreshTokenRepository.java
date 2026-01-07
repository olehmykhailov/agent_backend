package com.example.demo.auth.datalayer.repositories;

import com.example.demo.auth.datalayer.entities.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {
    Optional<RefreshTokenEntity> findByTokenHash(String tokenHash);

    void deleteByUserId(UUID userId);

    boolean existsByTokenHash(String tokenHash);

    List<RefreshTokenEntity> findAllByUserIdAndRevokedFalse(UUID userId);

}
