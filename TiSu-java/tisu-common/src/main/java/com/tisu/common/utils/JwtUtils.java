package com.tisu.common.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.user-expiration}")
    private long userExpiration;

    @Value("${jwt.admin-expiration}")
    private long adminExpiration;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateUserToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("type", "user")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + userExpiration))
                .signWith(getKey())
                .compact();
    }

    public String generateAdminToken(Long adminId) {
        return Jwts.builder()
                .subject(String.valueOf(adminId))
                .claim("type", "admin")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + adminExpiration))
                .signWith(getKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    public Long getAdminId(String token) {
        return Long.parseLong(parseToken(token).getSubject());
    }

    public boolean isValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
