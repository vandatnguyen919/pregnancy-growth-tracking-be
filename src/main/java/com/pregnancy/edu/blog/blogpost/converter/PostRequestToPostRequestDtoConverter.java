package com.pregnancy.edu.blog.blogpost.converter;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PostRequestToPostRequestDtoConverter implements Converter<BlogPost, BlogPostRequestDto> {
    @Override
    public BlogPostRequestDto convert(BlogPost source) {
        return new BlogPostRequestDto(
                source.getHeading(),
                source.getContent(),
                source.getPageTitle(),
                source.getShortDescription(),
                source.getFeaturedImageUrl(),
                source.isVisible()
        );
    }
}
