package com.quiz_study.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long accessValidMs;
    private final long refreshValidMs;

    public JwtTokenProvider(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-valid-ms}") long accessValidMs,
            @Value("${app.jwt.refresh-valid-ms}") long refreshValidMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessValidMs = accessValidMs;
        this.refreshValidMs = refreshValidMs;
    }

    public String generateAccessToken(String subject, Map<String, Object> claims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessValidMs);
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String subject) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshValidMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public String getSubject(String token) {
        return parse(token).getBody().getSubject();
    }
}
