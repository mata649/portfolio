package com.mata649.portfolio.authentication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${custom.security.jwt.expiration-in-minutes}")
    private Long EXPIRATION_IN_MINUTES;

    @Value("${custom.security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(String subject, Map<String, Object> extraClaims) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expDate = new Date((EXPIRATION_IN_MINUTES * 10 * 1000) + issuedAt.getTime());
        return Jwts.builder().claims(extraClaims)
                .header()
                .type("JWT")
                .and()
                .subject(subject)
                .issuedAt(issuedAt)
                .expiration(expDate)
                .signWith(generateKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey generateKey() {
        byte[] key = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(key);
    }

    public String extractUsername(String jwt) {
        return extractAllClaims(jwt).getSubject();
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
