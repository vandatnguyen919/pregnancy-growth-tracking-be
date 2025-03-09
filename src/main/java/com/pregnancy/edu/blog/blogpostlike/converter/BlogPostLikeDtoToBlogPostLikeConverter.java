package com.pregnancy.edu.blog.blogpostlike.converter;

import com.pregnancy.edu.blog.blogpost.BlogPost;
import com.pregnancy.edu.blog.blogpostlike.BlogPostLike;
import com.pregnancy.edu.blog.blogpostlike.dto.BlogPostLikeDto;
import com.pregnancy.edu.myuser.MyUser;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class BlogPostLikeDtoToBlogPostLikeConverter implements Converter<BlogPostLikeDto, BlogPostLike> {
    @Override
    public BlogPostLike convert(BlogPostLikeDto source) {
        BlogPostLike like = new BlogPostLike();

        BlogPost blogPost = new BlogPost();
        blogPost.setId(source.blogPostId());
        like.setBlogPost(blogPost);

        MyUser user = new MyUser();
        user.setId(source.userId());
        like.setUser(user);

        return like;
    }
}