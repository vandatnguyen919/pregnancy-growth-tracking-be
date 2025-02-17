package com.pregnancy.edu.blog.blogpostcomment.converter;

import com.pregnancy.edu.blog.blogpost.BlogPostService;
import com.pregnancy.edu.blog.blogpostcomment.BlogPostComment;
import com.pregnancy.edu.blog.blogpostcomment.dto.CommentDto;
import com.pregnancy.edu.myuser.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CommentDtoToCommentConverter implements Converter<CommentDto, BlogPostComment> {
    private final BlogPostComment blogPostComment = new BlogPostComment();

    @Override
    public BlogPostComment convert(CommentDto source) {
        blogPostComment.setContent(source.content());
        blogPostComment.setUpdatedAt(formatStringToDateTime(source.updatedAt()));
        blogPostComment.setCreatedAt(formatStringToDateTime(source.createdAt()));
        return blogPostComment;
    }

    public LocalDateTime formatStringToDateTime(String dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }
}
