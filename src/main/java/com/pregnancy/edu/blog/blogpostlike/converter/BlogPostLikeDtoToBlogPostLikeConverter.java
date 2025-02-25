package com.pregnancy.edu.blog.blogpostlike.converter;

import com.pregnancy.edu.blog.blogpostlike.BlogPostLike;
import com.pregnancy.edu.blog.blogpostlike.dto.BlogPostLikeDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BlogPostLikeDtoToBlogPostLikeConverter implements Converter<BlogPostLikeDto, BlogPostLike> {
    @Override
    public BlogPostLike convert(BlogPostLikeDto source) {
        BlogPostLike like = new BlogPostLike();
        like.getBlogPost().setId(source.blogPostId());
        like.getUser().setId(source.userId());
        return like;
    }
}