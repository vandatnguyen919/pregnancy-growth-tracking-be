package com.pregnancy.edu.myuser.dto;

import jakarta.validation.constraints.NotEmpty;

public record UserDto(Long id,
//                      @NotEmpty(message = "full name is required.")
                      String fullName,
                      @NotEmpty(message = "email is required.")
                      String email,
                      @NotEmpty(message = "username is required.")
                      String username,
                      boolean enabled,
                      boolean verified,
                      @NotEmpty(message = "role is required.")
                      String role) {
}