package com.pregnancy.edu.blog.blogpostlike.converter;

import com.pregnancy.edu.blog.blogpostlike.BlogPostLike;
import com.pregnancy.edu.blog.blogpostlike.dto.BlogPostLikeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BlogPostLikeToBlogPostLikeDtoConverter implements Converter<BlogPostLike, BlogPostLikeDto> {
    @Override
    public BlogPostLikeDto convert(BlogPostLike source) {
        return new BlogPostLikeDto(
                source.getId(),
                source.getBlogPost().getId(),
                source.getUser().getId(),
                source.getUser().getUsername()
        );
    }
}