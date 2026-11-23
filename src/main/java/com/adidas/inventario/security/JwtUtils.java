package com.adidas.inventario.security;

import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final String ISSUER = "adidas-inventario";

    private final SecretKey key;
    private final long expiration;

    public JwtUtils(
            @Value("${jwt.secret.key}") String secret,
            @Value("${jwt.expiration.ms}") long expiration) {

        this.key = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret));

        this.expiration = expiration;
    }

    public String generateToken(
            String username,
            String idUsu) {

        Instant now = Instant.now();

        return Jwts.builder()
                .issuer(ISSUER)
                .subject(username)
                .claim("idUsu", idUsu)
                .issuedAt(Date.from(now))
                .expiration(Date.from(
                        now.plusMillis(expiration)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Claims getClaimsFromToken(String token) {

        return Jwts.parser()
                .requireIssuer(ISSUER)
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}