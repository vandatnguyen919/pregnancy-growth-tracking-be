package com.pregnancy.edu.blog.blogpostlike.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BlogPostLikeDto(
        Long id,
        @NotNull(message = "Blog post ID is required")
        Long blogPostId,
        @NotNull(message = "User ID is required")
        Long userId,
        String username
) {}
