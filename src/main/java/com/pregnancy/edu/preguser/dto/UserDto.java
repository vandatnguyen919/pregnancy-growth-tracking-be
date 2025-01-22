package com.pregnancy.edu.preguser.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(Long id,
                      @NotEmpty(message = "email is required.")
                      String email,
                      @NotEmpty(message = "username is required.")
                      String username,
                      boolean enabled,
                      @NotEmpty(message = "role is required.")
                      String role) {
}