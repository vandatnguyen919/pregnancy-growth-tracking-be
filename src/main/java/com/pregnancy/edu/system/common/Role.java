package com.pregnancy.edu.system.common;

import lombok.Getter;

@Getter
public enum Role {
    USER("user"),
    ADMIN("admin"),
    EXPERT("expert"),
    MEMBER("member");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
