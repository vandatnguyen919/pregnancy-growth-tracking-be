package com.pregnancy.edu.blog.tag.dto;

import jakarta.validation.constraints.NotEmpty;

public record TagDto(
        Long id,
        @NotEmpty(message = "Tag name is required")
        String name,
        Integer blogPostCount
) {}