package com.pregnancy.edu.blog.blogpost.converter;

import com.pregnancy.edu.blog.tag.Tag;
import org.springframework.core.convert.converter.Converter;
import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
                source.isVisible(),
                source.getComments().size(),
                source.getLikes().size(),
                source.getTags().stream().map(Tag::getName).collect(Collectors.toList())
                );
    }
}
