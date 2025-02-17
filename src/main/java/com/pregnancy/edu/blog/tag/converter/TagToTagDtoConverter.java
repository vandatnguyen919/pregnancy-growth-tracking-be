package com.pregnancy.edu.blog.tag.converter;

import com.pregnancy.edu.blog.tag.Tag;
import com.pregnancy.edu.blog.tag.dto.TagDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TagToTagDtoConverter implements Converter<Tag, TagDto> {
    @Override
    public TagDto convert(Tag source) {
        return new TagDto(
                source.getId(),
                source.getName(),
                source.getBlogPosts().isEmpty() ? 0 : source.getBlogPosts().size()
        );
    }
}