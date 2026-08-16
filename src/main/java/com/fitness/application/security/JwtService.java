package com.fitness.application.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.fitness.application.users.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;

@Service
public class JwtService {

    private final MacAlgorithm alg = Jwts.SIG.HS256;

    private final String SECRET_STRING = "your-super-secret-key-that-must-be-at-least-32-bytes-long!";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    
    private final long JWT_EXPIRATION = 720000000;

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
