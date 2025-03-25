package com.pregnancy.edu.system.common;

import lombok.Getter;

@Getter
public enum Role {
<<<<<<< HEAD
    USER("user"),
=======
    USER("user"),   
>>>>>>> ddd5ca6315ea80da9b7e62c22eeb04b6003cc784
    ADMIN("admin"),
    MEMBER("member");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }
}
