package com.pregnancy.edu.blogpost.dto;

import jakarta.validation.constraints.NotEmpty;

public record BlogPostDto(Long id,
                          @NotEmpty(message = "heading is required.")
                          String heading,
                          @NotEmpty(message = "content is required.")
                          String content,
                          String pageTitle,
                          String shortDescription,
                          String featuredImageUrl,
                          Boolean isVisible) {
}
