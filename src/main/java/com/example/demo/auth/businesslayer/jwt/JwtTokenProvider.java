package com.example.demo.auth.businesslayer.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import java.util.function.Function;


@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenValidityMs;
    private final long refreshTokenValidityMs;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") long accessTokenValidityMs,
            @Value("${jwt.refresh.expiration}") long refreshTokenValidityMs) {

        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityMs = accessTokenValidityMs;
        this.refreshTokenValidityMs = refreshTokenValidityMs;
    }

    public String generateToken(UUID userId, long expiryTime) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expiryTime);
        JwtBuilder builder = Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(key, SignatureAlgorithm.HS256);
        return builder.compact();
    }

    public String generateAccessToken(UUID userId) {
        return generateToken(userId, accessTokenValidityMs);
    }

    private Claims extractAllClaims(String token) {

        JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();

        return parser.parseClaimsJws(token).getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public UUID getUserIdFromToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return UUID.fromString(claims.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private Date getExpirationDate(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    public boolean validateToken(String token, UUID userId) {
        final UUID userIdFromToken = getUserIdFromToken(token);
        return !isTokenExpired(token) && userIdFromToken.equals(userId);
    }


}