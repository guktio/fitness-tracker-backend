package com.fitness.application.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fitness.application.users.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractToken(request.getHeader("Authorization"));

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtService.validateJwt(token)) {
                String uuid = jwtService.extractUuid(token);

                userRepository.findById(UUID.fromString(uuid)).ifPresent(user -> {
                    UserDetailsImpl userDetails = new UserDetailsImpl(user);
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                });
            } else {
                logger.warn("Invalid JWT token");
            }
        } catch (Exception e) {
            logger.warn("Failed to process JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
}
