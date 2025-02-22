package com.pregnancy.edu.blog.blogpostcomment.dto;

public record CommentDto(Long id,
                         String content,
                         String createdAt,
                         String updatedAt,
                         Long blogPostId,
                         Long userId) {
}
