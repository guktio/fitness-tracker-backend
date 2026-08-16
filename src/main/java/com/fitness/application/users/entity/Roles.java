package com.fitness.application.users.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    ADMIN, USER, MODERATOR;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
