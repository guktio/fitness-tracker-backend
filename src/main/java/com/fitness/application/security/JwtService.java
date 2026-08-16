package com.fitness.application.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fitness.application.users.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    public JwtService(@Value("${jwt.secret-key}") String SECRET_STRING){
        if (SECRET_STRING == null || SECRET_STRING.isBlank()) {
            throw new IllegalStateException("Secret key is null set up jwt.secret-key value!");
        }
        this.key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
        log.debug(SECRET_STRING);
    }

    private final MacAlgorithm alg = Jwts.SIG.HS256;

    private final SecretKey key;
    
    private final long JWT_EXPIRATION = 720000000L;

    public String generateJwt(User user) {
        Date now = new Date();
        Date expDate = new Date(now.getTime()+JWT_EXPIRATION);
        String jws = Jwts.builder()
                        .subject(user.getUuid().toString())
                        .claim("email", user.getEmail())
                        .claim("roles", user.getRoles())
                        .signWith(key, alg)
                        .issuedAt(now)
                        .expiration(expDate)
                        .compact();
        return jws;
    }

    public boolean validateJwt(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUuid(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("sub", String.class);
    }
}
