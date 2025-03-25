package com.pregnancy.edu.blog.blogpost.dto;

import com.pregnancy.edu.myuser.dto.UserDto;
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
                          boolean isVisible,
                          Integer likeQuantity,
                          List<String> nameTags,
                          UserDto userDto) {
}
