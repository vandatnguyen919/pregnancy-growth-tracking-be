package com.pregnancy.edu.blog.blogpostcomment.converter;

import com.pregnancy.edu.blog.blogpostcomment.BlogPostComment;
import com.pregnancy.edu.blog.blogpostcomment.dto.CommentDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommentDtoToCommentConverter implements Converter<CommentDto, BlogPostComment> {

    @Override
    public BlogPostComment convert(CommentDto source) {
        BlogPostComment blogPostComment = new BlogPostComment();
        blogPostComment.setContent(source.content());
        blogPostComment.setUpdatedAt(formatStringToDateTime(source.updatedAt()));
        blogPostComment.setCreatedAt(formatStringToDateTime(source.createdAt()));
        return blogPostComment;
    }

    public LocalDateTime formatStringToDateTime(String dateTime) {
        if(dateTime == null || dateTime.equals("")) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }
}
