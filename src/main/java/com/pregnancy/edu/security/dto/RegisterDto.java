package com.pregnancy.edu.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterDto(
        @NotEmpty(message = "Full name is required.")
        String fullName,
        @Email @NotEmpty(message = "Email is required.")
        String email,
        @NotEmpty(message = "Password is required.")
        String password,
        @NotEmpty(message = "Confirm password is required.")
        String confirmPassword,
        String role
) {
}
