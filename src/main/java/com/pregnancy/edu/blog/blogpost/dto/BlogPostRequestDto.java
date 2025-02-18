package com.pregnancy.edu.blog.blogpost.dto;

import jakarta.validation.constraints.NotEmpty;

public record BlogPostRequestDto(@NotEmpty(message = "heading is required.")
                                 String heading,
                                 @NotEmpty(message = "content is required.")
                                 String content,
                                 String pageTitle,
                                 String shortDescription,
                                 String featuredImageUrl,
                                 Boolean isVisible) {
}
