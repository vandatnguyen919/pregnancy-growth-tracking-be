package com.pregnancy.edu.blog.blogpost.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record BlogPostDto(Long id,
                          @NotEmpty(message = "heading is required.")
                          String heading,
                          @NotEmpty(message = "content is required.")
                          String content,
                          String pageTitle,
                          String shortDescription,
                          String featuredImageUrl,
                          Boolean isVisible,
                          Integer commentQuantity,
                          Integer likeQuantity,
                          List<String> nameTags) {
}
