package com.pregnancy.edu.myuser.dto;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String fullName,
//        @NotEmpty(message = "email is required.")
        String email,
//        @NotEmpty(message = "username is required.")
        String username,
        boolean enabled,
        boolean verified,
//        @NotEmpty(message = "role is required.")
        String role,
        // Added attributes
        String phoneNumber,
        LocalDateTime dateOfBirth,
        String avatarUrl,
        Boolean gender,
        String bloodType,
        String symptoms,
        String nationality,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}