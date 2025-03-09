package com.pregnancy.edu.system.common;

import lombok.Getter;

@Getter
public enum Role {
    AUTHENTICATED_USER("authenticatedUser"),
    ADMIN("admin"),
    MEMBER("member");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
