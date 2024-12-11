package com.example.JwtPractice.domain.token.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    Boolean existsByUserId(Long userId);

    Optional<TokenEntity> findByRefreshToken(String token);

    Optional<TokenEntity> findByUserId(Long id);
}
