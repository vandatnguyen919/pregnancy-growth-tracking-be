package com.pregnancy.edu.blog.blogpost.converter;

import com.pregnancy.edu.blog.tag.Tag;
import com.pregnancy.edu.myuser.converter.UserToUserDtoConverter;
import org.springframework.core.convert.converter.Converter;
import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpost.dto.BlogPostDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BlogPostToBlogPostDtoConverter implements Converter<BlogPost, BlogPostDto> {

    private final UserToUserDtoConverter userToUserDtoConverter;

    public BlogPostToBlogPostDtoConverter(UserToUserDtoConverter userToUserDtoConverter) {
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

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
                source.getComments().isEmpty() ? 0 : source.getComments().size(),
                source.getLikes().isEmpty() ? 0 : source.getLikes().size(),
                source.getTags().stream().map(Tag::getName).collect(Collectors.toList()),
                userToUserDtoConverter.convert(source.getUser())
                );
    }
}
