package com.pregnancy.edu.system.consts;

import lombok.Getter;

@Getter
public enum Role {
    USER("user"),
    ADMIN("admin"),
    EXPERT("expert");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
