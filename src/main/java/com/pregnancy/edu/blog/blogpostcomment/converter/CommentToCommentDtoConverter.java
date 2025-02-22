package com.pregnancy.edu.blog.blogpostcomment.converter;

import com.pregnancy.edu.blog.blogpostcomment.BlogPostComment;
import com.pregnancy.edu.blog.blogpostcomment.dto.CommentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommentToCommentDtoConverter implements Converter<BlogPostComment, CommentDto> {
    @Override
    public CommentDto convert(BlogPostComment source) {
        return new CommentDto(
                source.getId(),
                source.getContent(),
                formatDateTimeToString(source.getCreatedAt()),
                formatDateTimeToString(source.getUpdatedAt()),
                source.getBlogPost().getId(),
                source.getUser().getId()
        );
    }
    public String formatDateTimeToString(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}
