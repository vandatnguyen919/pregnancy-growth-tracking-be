package com.pregnancy.edu.blog.blogpost.converter;

import org.springframework.core.convert.converter.Converter;
import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostDto;
import org.springframework.stereotype.Component;

@Component
public class BlogPostToBlogPostDtoConverter implements Converter<BlogPost, BlogPostDto> {
    @Override
    public BlogPostDto convert(BlogPost source) {
        return new BlogPostDto(
                source.getId(),
                source.getHeading(),
                source.getContent(),
                source.getPageTitle(),
                source.getShortDescription(),
                source.getFeaturedImageUrl(),
                source.isVisible()
        );
    }
}
